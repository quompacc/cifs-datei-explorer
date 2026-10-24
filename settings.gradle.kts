pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "CIFS Datei-Explorer"

include(":app")
include(":common")
include(":data:data")
include(":data:storage:interfaces")
include(":data:storage:manager")
include(":data:storage:apache")
include(":data:storage:jcifsng")
include(":data:storage:smbj")
include(":domain")
include(":presentation")

// Git submodule, only present after "git submodule update --init"
if (file("tools/string_converter").list()?.isNotEmpty() == true) {
    includeBuild("tools/string_converter")
}
