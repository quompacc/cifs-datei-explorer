import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

val applicationId: String by rootProject.extra
val appPackageId: String by rootProject.extra
val javaVersion: JavaVersion by rootProject.extra
val androidCompileSdk: Int by rootProject.extra
val androidMinSdk: Int by rootProject.extra
val appVersionName: String by rootProject.extra
val appVersionCode: Int by rootProject.extra

val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) load(f.inputStream())
}

android {
    compileSdk = androidCompileSdk
    namespace = applicationId
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    defaultConfig {
        applicationId = appPackageId
        minSdk = androidMinSdk
        targetSdk = androidCompileSdk
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        base.archivesName.set("CIFS-Datei-Explorer-${versionName}")
    }

    // Release signing: environment variables (CI) take precedence over local.properties (local).
    // Without a keystore (e.g. F-Droid build server) the release APK is built unsigned.
    val keystoreFile = rootProject.file(System.getenv("KEYSTORE_FILE") ?: localProps["KEYSTORE_FILE"] as? String ?: "cifs-datei-explorer.jks")
    val hasKeystore = keystoreFile.exists()

    signingConfigs  {
        getByName("debug") {
            storeFile = file("$rootDir/debug.keystore")
        }
        if (hasKeystore) {
            create("release") {
                storeFile = keystoreFile
                storePassword = System.getenv("KEYSTORE_PASSWORD") ?: localProps["KEYSTORE_PASSWORD"] as? String ?: ""
                keyAlias = System.getenv("KEY_ALIAS") ?: localProps["KEY_ALIAS"] as? String ?: ""
                keyPassword = System.getenv("KEY_PASSWORD") ?: localProps["KEY_PASSWORD"] as? String ?: ""
            }
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = "D"
        }
        release {
            isMinifyEnabled = false
            if (hasKeystore) signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.majorVersion))
        }
    }

    packaging {
        // For commons-vfs2-jackrabbit2
        resources.excludes.addAll(setOf(
            "META-INF/*",
            "META-INF/versions/*/OSGI-INF/MANIFEST.MF",
        ))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to arrayOf("*.jar"))))
    implementation(project(":common"))
    implementation(project(":presentation"))
    implementation(project(":domain"))

    implementation(libs.androidx.appcompat)
    implementation(libs.hilt.android)
    implementation(libs.androidx.work.runtime)
    ksp(libs.hilt.android.compiler)
}
