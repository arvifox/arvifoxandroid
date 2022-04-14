dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
//        jcenter()
        maven("https://csspeechstorage.blob.core.windows.net/maven/")
        maven("https://jitpack.io")
    }
}

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
//    resolutionStrategy {
//        eachPlugin {
//            if (requested.id.id.startsWith("com.android")) {
//                useModule("com.android.tools.build:gradle:7.0.0")
//            }
//            if (requested.id.id.startsWith("org.jetbrains.kotlin")) {
//                useVersion("1.6.10")
//            }
//        }
//    }
}

rootProject.name = "arvifoxandroid"
include(":app")
