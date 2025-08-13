import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed

fun secret(name: String): String? {
    val fileProperties = File(rootProject.projectDir.absolutePath, "local.properties")
    val pr = runCatching { FileInputStream(fileProperties) }.getOrNull()?.let { file ->
        Properties().apply {
            load(file)
        }
    }
    return pr?.getProperty(name) ?: System.getenv(name)
}

fun maybeWrapQuotes(s: String): String {
    return if (s.startsWith("\"")) s else "\"" + s + "\""
}

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "com.arvifox.arvi"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.arvifox.arvi"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        manifestPlaceholders["googleGeoApiKey"] = secret("googleGeoApiKey")!!
//        resourceConfigurations.addAll(listOf("en", "fr", "ru", "en_XA", "ar_XB"))
        buildConfigField("String", "ARVI_API_URL", maybeWrapQuotes("ARVI_API_URL"))
        buildConfigField("String", "FLICKR_KEY", maybeWrapQuotes("FLICKR_KEY"))
        buildConfigField("String", "TMDB_KEY", maybeWrapQuotes("TMDB_KEY"))
    }
    buildTypes {
        debug {
            isPseudoLocalesEnabled = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            //signingConfig = signingConfigs.configrelease
        }
    }
    buildFeatures {
        //viewBinding true
        dataBinding = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += listOf("META-INF/DEPENDENCIES", "META-INF/LICENSE", "META-INF/LICENSE-notice.md", "META-INF/LICENSE.md", "META-INF/LICENSE.txt", "META-INF/license.txt", "META-INF/NOTICE", "META-INF/NOTICE.txt", "META-INF/notice.txt", "META-INF/ASL2.0", "META-INF/AL2.0", "META-INF/LGPL2.1", "META-INF/INDEX.LIST", "META-INF/io.netty.versions.properties")
        }
    }
    sourceSets.getByName("main") {
        res.srcDirs("src/main/res", "src/main/res-some")
    }
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

class RoomSchemaArgProvider(
    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File,
) : CommandLineArgumentProvider {

    override fun asArguments(): MutableIterable<String> {
        return mutableListOf("room.schemaLocation=${schemaDir.path}")
    }
}

ksp {
    arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//    implementation(files("/path/path/file.jat"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.google.android.material)
    implementation(libs.constraint.layout.view)
    implementation(libs.constraint.layout.compose)
    implementation(libs.androidx.annotation)

    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)

    implementation(libs.coil.compose)
    implementation(libs.coil.ktor)
    implementation(libs.ktor.android)

    //test
//    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.activity.compose)
    implementation(libs.androidx.material3)
    implementation(libs.compose.navigation)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.compose.material)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    testImplementation(libs.tests.junit)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.tests.archcore)

    //multidex
    //implementation("androidx.multidex:multidex:2.0.1")

    implementation(libs.androidx.palette.ktx)

    //arch
    implementation(libs.androidx.work.runtime.ktx)

    //lifecycler
    // ViewModel and LiveData
    implementation(libs.androidx.lifecycle.extensions)
    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation(libs.androidx.lifecycle.common.java8)
    // optional - ReactiveStreams support for LiveData
    implementation(libs.androidx.lifecycle.reactivestreams.ktx)
    // optional - Test helpers for LiveData
    testImplementation(libs.tests.archcore)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    // view model scope
    implementation(libs.lifecycle.viewmodel.ktx)
    // lifecycle scope
    implementation(libs.lifecycle.runtime.ktx)
    // livedata scope
    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //region room

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)
    // optional - Guava support for Room, including Optional and ListenableFuture
    //implementation "androidx.room:room-guava:$room_version"
    // Test helpers
    testImplementation(libs.androidx.room.testing)

    //endregion

    // CameraX core library using camera2 implementation
    implementation(libs.androidx.camera.camera2)
    // CameraX Lifecycle Library
    implementation(libs.androidx.camera.lifecycle)
    // CameraX View class
    implementation(libs.androidx.camera.view)

    //moshi
//    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    //implementation 'org.jetbrains.kotlinx:kotlinx-datetime:0.1.0'

    implementation(libs.androidx.exifinterface)

    //retrofit okhttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.moshi)
    implementation(libs.retrofit.mock)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)

    // AR core, Sceneform
//    implementation(libs.core)
//    implementation(libs.sceneform.core)
//    implementation(libs.sceneform.ux)

    //firebase
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.core)
    implementation(libs.play.services.auth)
    implementation(libs.play.services.base)
    implementation(libs.play.services.gcm)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    //concurrent
    implementation(libs.androidx.concurrent.futures)

    // Kotlin Coroutines
    implementation(libs.coroutine.android)
    implementation(libs.coroutine.core)
    implementation(libs.coroutine.play.services)
    testImplementation(libs.coroutine.core)
    testImplementation(libs.coroutine.debug)
    testImplementation(libs.coroutine.test)

    implementation(libs.kotlinx.immutable.collection)

    //picasso
    implementation("com.squareup.picasso:picasso:2.71828") {
        exclude("com.android.support")
    }
    // glide
    implementation(libs.glide)

    // viewpager
    implementation(libs.androidx.viewpager2)

    //ar vr vision
    implementation(libs.google.api.services.vision)
    implementation(libs.google.api.client.android)
    implementation(libs.google.http.client.gson)

    // ConcealerNestedScrollView & ConcealerRecyclerView
    // A library to make views hide from top and bottom while scrolling a custom NestedScrollView and\or a custom RecyclerView
    // https://github.com/SIMMORSAL/ConcealerNestedScrollView-ConcealerRecyclerView
    //implementation 'com.simmorsal.library:concealer_nested_scroll_view:2.0.0'
    //implementation 'com.dc.easyadapter:easyadapter:2.0.3'


    // billing lib
    implementation(libs.billing)

    // Speech SDK
    implementation(libs.client.sdk)

    //biometric
    implementation(libs.androidx.biometric)

    implementation(libs.androidx.browser)

    // https://github.com/stfalcon-studio/StfalconImageViewer
    //implementation 'com.github.stfalcon:stfalcon-imageviewer:1.0.1'

    // Play Install Referrer Library
    // https://developer.android.com/google/play/installreferrer/library.html
    //implementation 'com.android.installreferrer:installreferrer:2.2'

    //https://github.com/str4d/ed25519-java
    implementation(libs.eddsa)

    // Pure Java implementation of EdDSA-SHA3
    implementation(libs.ed25519.sha3.java)
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

//sceneform.asset(
//    "sampledata/model.fbx",
//    "default",
//    "sampledata/model.sfa",
//    "src/main/res/raw/model"
//)

//task copyDeps(type: Copy) {
//  from configurations.arvifoxconf
//  into './depsdir'
//}
