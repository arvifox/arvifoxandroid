// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlin_version = "1.6.10"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("com.google.gms:google-services:4.3.10")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.google.ar.sceneform:plugin:1.17.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.6.10")
        //classpath "android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0"

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }

//    configurations.all {
//        resolutionStrategy {
//            force("net.sf.proguard:proguard-base:6.2.1")
//        }
//    }
    
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://csspeechstorage.blob.core.windows.net/maven/")
        maven("https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

//tasks.processResources.configure {
//    duplicatesStrategy = DuplicatesStrategy.INCLUDE
//    from("gradle/libs.versions.toml")
//}
