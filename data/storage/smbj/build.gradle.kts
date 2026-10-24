plugins {
    alias(libs.plugins.android.library)
}

val applicationId: String by rootProject.extra
val javaVersion: JavaVersion by rootProject.extra
val androidCompileSdk: Int by rootProject.extra
val androidMinSdk: Int by rootProject.extra

android {
    compileSdk = androidCompileSdk
    namespace = "${applicationId}.data.storage.smbj"

    defaultConfig {
        minSdk = androidMinSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

}

dependencies {
    implementation(project(":common"))
    implementation(project(":data:storage:interfaces"))

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.smbj)
    implementation(libs.dcerpc) {
        exclude(group = "org.bouncycastle", module = "bcprov-jdk15on") // Duplicate with JCIFS-NG
    }
    implementation(libs.listenablefuture) // to avoid conflict

    testImplementation(libs.junit)
}
