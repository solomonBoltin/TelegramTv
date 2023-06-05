plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.relay") version "0.3.02"
    kotlin("kapt")
}

android {
    namespace = "com.solomonboltin.telegramtv"
    compileSdk = 33

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    defaultConfig {
        applicationId = "com.solomonboltin.telegramtv"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        ndk {
            abiFilters.add("x86")
            abiFilters.add("x86_64")
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.tv:tv-foundation:1.0.0-alpha03")
    implementation("androidx.room:room-common:2.5.1")
    val composeBom = platform("androidx.compose:compose-bom:2022.12.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.appcompat:appcompat-resources:1.5.1")
    implementation("com.google.accompanist:accompanist-appcompat-theme:0.25.1")
    val compose_version = "1.3.0"

    implementation("androidx.compose.animation:animation-core:$compose_version")
    implementation("androidx.compose.animation:animation:$compose_version")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.foundation:foundation:$compose_version")
    implementation("androidx.compose.foundation:foundation-layout:$compose_version")
    implementation("androidx.compose.ui:ui-geometry:$compose_version")
    implementation("androidx.compose.ui:ui-graphics:$compose_version")
    implementation("androidx.compose.foundation:foundation-layout:$compose_version")
    implementation("androidx.compose.runtime:runtime-livedata:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.material:material-icons-core:$compose_version")
    implementation("androidx.compose.material:material-icons-extended:$compose_version")
    implementation("androidx.compose.runtime:runtime-rxjava2:$compose_version")
    implementation("androidx.compose.ui:ui-text:$compose_version")
    implementation("androidx.compose.ui:ui-util:$compose_version")
    implementation("androidx.compose.ui:ui-viewbinding:$compose_version")
    implementation("androidx.compose.ui:ui-tooling:$compose_version")
    implementation("androidx.activity:activity-compose:1.3.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.3.2")

    implementation("androidx.compose.ui:ui-tooling-preview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    implementation("androidx.core:core-ktx:1.7.0")

    implementation("androidx.leanback:leanback:1.0.0")
    implementation("com.google.zxing:core:3.4.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation(project(":libtd"))

    val koin_version = "3.3.2"
    val koin_android_compose_version = "3.4.1"

    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-android:$koin_version")
    implementation("io.insert-koin:koin-androidx-navigation:$koin_version")
    implementation("io.insert-koin:koin-androidx-compose:$koin_android_compose_version")
    implementation("io.insert-koin:koin-android-compat:$koin_version")

    androidTestImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")

    implementation("org.slf4j:slf4j-android:1.7.30")

    implementation("com.google.android.exoplayer:exoplayer:2.18.6")
    implementation ("androidx.room:room-runtime:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")

    kapt ("androidx.room:room-compiler:2.5.1")
}
