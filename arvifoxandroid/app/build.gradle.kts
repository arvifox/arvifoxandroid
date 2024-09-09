plugins {
    id("maven-publish")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
    alias(libs.plugins.googleServicesPlugin)
    alias(libs.plugins.firebaseCrashlyticsPlugin)
    alias(libs.plugins.firebaseAppDistributionPlugin)
    alias(libs.plugins.triplet)
    id("kotlin-parcelize")
    id("org.jetbrains.kotlinx.kover")
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.arvifox.arvi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arvifox.arvi"
        minSdk = rootProject.extra["minSdk"] as? Int?
        targetSdk = 34
        versionCode = 6
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
        animationsDisabled = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += listOf("META-INF/DEPENDENCIES", "META-INF/LICENSE", "META-INF/LICENSE-notice.md", "META-INF/LICENSE.md", "META-INF/LICENSE.txt", "META-INF/license.txt", "META-INF/NOTICE", "META-INF/NOTICE.txt", "META-INF/notice.txt", "META-INF/ASL2.0", "META-INF/AL2.0", "META-INF/LGPL2.1", "META-INF/INDEX.LIST", "META-INF/io.netty.versions.properties")
        }
    }
    applicationVariants.all {
        val variant = this
        this.outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach {
                it.outputFileName = "ArviFox_${variant.versionName}_${variant.versionCode}_${variant.flavorName}_${variant.buildType.name}.apk"
            }
    }
    configurations.all {
        //exclude(module = "")
        resolutionStrategy.eachDependency {
            when {
                requested.name == "core-ptx" -> useVersion("77.88.99")
            }
        }
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.coreKtxDep)
    implementation(libs.runtimeKtxDep)
    implementation(libs.composeActivityDep)

    implementation(platform(libs.composeBomDep))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junitDep)
    androidTestImplementation(libs.androidxTestExtJunitDep)
    androidTestImplementation(libs.androidxTestEspressoCoreDep)
    androidTestImplementation(platform(libs.composeBomDep))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.workManagerDep)

    implementation(libs.daggerDep)
    kapt(libs.daggerKaptDep)
    implementation(libs.hiltWorkManagerDep)
    kapt(libs.hiltWorkManagerKaptDep)

    implementation(libs.lifecycleProcessDep)
    kapt(libs.lifecycleKaptDep)
}

kapt {
    correctErrorTypes = true
}
