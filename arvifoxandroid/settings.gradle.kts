pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
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
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
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

rootProject.name = "arvifoxandroid"
include(":app")
