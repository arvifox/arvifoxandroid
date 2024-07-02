import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

buildscript {
    repositories {
        //jcenter()
    }

    dependencies {
        //classpath "org.jetbrains.dokka:dokka-android-gradle-plugin:0.9.17"
    }
}

plugins {
    kotlin("plugin.serialization")
    id("com.android.application")
    kotlin("android")
    //id("androidx.navigation.safeargs")
    kotlin("kapt")
    id("kotlin-parcelize")
    //id("org.jetbrains.dokka-android")
    id("com.google.gms.google-services")
    id("com.google.ar.sceneform.plugin")
}

// Creates a variable called keystorePropertiesFile, and initializes it to the
// keys.properties file.
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream("mykeys.properties"))
// Initializes a new Properties() object called keystoreProperties.
//val keystoreProperties = gradleLocalProperties(qwe)
// Loads the keys.properties file into the keystoreProperties object.
//keystoreProperties.load(new FileInputStream(keystorePropertiesFile))

android {
//    signingConfigs {
//        configrelease {
//            keyAlias keystoreProperties['keyAlias']
//            keyPassword keystoreProperties['keyPassword']
//            storeFile file(keystoreProperties['storeFile'])
//            storePassword keystoreProperties['storePassword']
//        }
//    }
    compileSdk = 34
    defaultConfig {
        applicationId = "com.arvifox.arvi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        manifestPlaceholders["googleGeoApiKey"] =
            keystoreProperties.getProperty("googleGeoApiKey").orEmpty()
        resourceConfigurations.addAll(listOf("en", "fr", "ru", "en_XA", "ar_XB"))
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }
    buildTypes {
        debug {
            isPseudoLocalesEnabled = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
            )
            //signingConfig = signingConfigs.configrelease
        }
        applicationVariants.all {
            buildConfigField(
                "String",
                "ARVI_API_URL",
                "\"" + keystoreProperties.getProperty("arviApiUrl") + "\""
            )
            buildConfigField(
                "String",
                "FLICKR_KEY",
                "\"" + keystoreProperties.getProperty("flickrKey") + "\""
            )
            buildConfigField(
                "String",
                "TMDB_KEY",
                "\"" + keystoreProperties.getProperty("tmdb") + "\""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        //viewBinding true
        dataBinding = true
        buildConfig = true
        //compose = true
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "compose version"
//    }
    packaging {
        resources {
            excludes += listOf("META-INF/DEPENDENCIES", "META-INF/LICENSE", "META-INF/LICENSE-notice.md", "META-INF/LICENSE.md", "META-INF/LICENSE.txt", "META-INF/license.txt", "META-INF/NOTICE", "META-INF/NOTICE.txt", "META-INF/notice.txt", "META-INF/ASL2.0", "META-INF/AL2.0", "META-INF/LGPL2.1", "META-INF/INDEX.LIST", "META-INF/io.netty.versions.properties")
        }
    }
    sourceSets.getByName("main") {
        res.srcDirs("src/main/res", "src/main/res-some")
    }
    namespace = "com.arvifox.arvi"
//    sourceSets {
//        main.res.srcDirs += ['src/main/res-some']
//        main.jniLibs.srcDirs += ['src/main/jniLibs']
//    }
//    testOptions {
//        unitTests.returnDefaultValues = true
//    }
//    ndk {
    /*
             * Sceneform is available for the following ABIs: arm64-v8a, armv7a,
             * x86_64 and x86. This sample app enables arm64-v8a to run on
             * devices and x86 to run on the emulator. Your application should
             * list the ABIs most appropriate to minimize APK size (arm64-v8a recommended).
             */
//        abiFilters 'arm64-v8a', 'x86'
//    }
//    androidExtensions {
//        experimental = true
//    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//    implementation(files("/path/path/file.jat"))
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.8.0")

    //test
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.test.ext:junit:1.2.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.work:work-testing:2.9.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    //multidex
    //implementation("androidx.multidex:multidex:2.0.1")

    implementation("androidx.palette:palette-ktx:1.0.0")

    //arch
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    //lifecycler
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    //kapt "androidx.lifecycle:lifecycle-compiler:$lifecycle_version" // For Kotlin use kapt instead of annotationProcessor
    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation("androidx.lifecycle:lifecycle-common-java8:2.8.2")
    // optional - ReactiveStreams support for LiveData
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.8.2")
    // optional - Test helpers for LiveData
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.2")
    // view model scope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    // lifecycle scope
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    // livedata scope
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // room

    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    // For Kotlin use kapt instead of annotationProcessor
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")
    // optional - Guava support for Room, including Optional and ListenableFuture
    //implementation "androidx.room:room-guava:$room_version"
    // Test helpers
    testImplementation("androidx.room:room-testing:2.6.1")

    //room end

    // CameraX core library using camera2 implementation
    implementation("androidx.camera:camera-camera2:1.3.4")
    // CameraX Lifecycle Library
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    // CameraX View class
    implementation("androidx.camera:camera-view:1.3.4")

    //moshi
//    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
//    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.2")

    //implementation 'org.jetbrains.kotlinx:kotlinx-datetime:0.1.0'

    implementation("androidx.exifinterface:exifinterface:1.3.7")

    //retrofit okhttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.retrofit2:retrofit-mock:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.8")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.12")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    // AR core, Sceneform
    implementation("com.google.ar:core:1.44.0")
    implementation("com.google.ar.sceneform:core:1.17.1")
    implementation("com.google.ar.sceneform.ux:sceneform-ux:1.17.1")

    //firebase
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.android.gms:play-services-base:18.5.0")
    implementation("com.google.android.gms:play-services-gcm:17.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    //concurrent
    implementation("androidx.concurrent:concurrent-futures:1.2.0")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:1.7.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    //picasso
    implementation("com.squareup.picasso:picasso:2.71828") {
        exclude("com.android.support")
    }
    // glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // viewpager
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    //ar vr vision
    implementation("com.google.apis:google-api-services-vision:v1-rev16-1.22.0")
    implementation("com.google.api-client:google-api-client-android:1.26.0")
    implementation("com.google.http-client:google-http-client-gson:1.43.3")

    // ConcealerNestedScrollView & ConcealerRecyclerView
    // A library to make views hide from top and bottom while scrolling a custom NestedScrollView and\or a custom RecyclerView
    // https://github.com/SIMMORSAL/ConcealerNestedScrollView-ConcealerRecyclerView
    //implementation 'com.simmorsal.library:concealer_nested_scroll_view:2.0.0'
    //implementation 'com.dc.easyadapter:easyadapter:2.0.3'


    // billing lib
    implementation("com.android.billingclient:billing:7.0.0")

    // Speech SDK
    implementation("com.microsoft.cognitiveservices.speech:client-sdk:1.8.0")

    //biometric
    implementation("androidx.biometric:biometric:1.1.0")

    implementation("androidx.browser:browser:1.8.0")

    // https://github.com/stfalcon-studio/StfalconImageViewer
    //implementation 'com.github.stfalcon:stfalcon-imageviewer:1.0.1'

    // Play Install Referrer Library
    // https://developer.android.com/google/play/installreferrer/library.html
    //implementation 'com.android.installreferrer:installreferrer:2.2'

    //crypto

    //https://github.com/str4d/ed25519-java
    implementation("net.i2p.crypto:eddsa:0.3.0")

    // Pure Java implementation of EdDSA-SHA3
    implementation("com.github.warchant:ed25519-sha3-java:2.0.1")
}

// configurations.forEach { it.exclude("com.google.guava", "listenablefuture") }
configurations {
    implementation.get().exclude(module = "guava-jdk5")
    all {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }
}

val arvifoxconf by configurations.creating

//dokka {
//    outputFormat = 'html'
//    outputDirectory = "$buildDir/kotlindoc"
//}

sceneform.asset(
    "sampledata/model.fbx",
    "default",
    "sampledata/model.sfa",
    "src/main/res/raw/model"
)

//task copyDeps(type: Copy) {
//  from configurations.arvifoxconf
//  into './depsdir'
//}
