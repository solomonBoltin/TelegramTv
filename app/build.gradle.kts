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
        minSdk = 21
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
        kotlinCompilerExtensionVersion = "1.4.2"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // UI
    implementation("androidx.core:core-ktx:1.10.1")
    implementation( platform("androidx.compose:compose-bom:2023.01.00"))
    implementation("androidx.compose.ui:ui-tooling")

    // Compose integration with activities
    implementation( "androidx.activity:activity-compose:1.7.2")

    // TV Compose
    val tv_compose_version = "1.0.0-alpha07"
    implementation( "androidx.tv:tv-foundation:$tv_compose_version")
    implementation ("androidx.tv:tv-material:$tv_compose_version")

    // view model
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // navigation
    implementation ("androidx.navigation:navigation-compose:2.5.3")
    // async image loading
    implementation ("io.coil-kt:coil-compose:2.2.2")


    // DB
    implementation("androidx.room:room-common:2.5.1")

    // Other
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.appcompat:appcompat-resources:1.5.1")
    implementation("com.google.accompanist:accompanist-appcompat-theme:0.25.1")
    implementation("androidx.leanback:leanback:1.0.0")

//    implementation("androidx.compose.material3:material3")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    debugImplementation("androidx.compose.ui:ui-tooling")


    // relay
    val compose_version = "1.4.2"
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.material:material-icons-core:$compose_version")
    implementation("androidx.compose.material:material-icons-extended:$compose_version")



    implementation("com.google.zxing:core:3.4.1")
//    implementation("androidx.core:core-ktx:1.9.0")
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
