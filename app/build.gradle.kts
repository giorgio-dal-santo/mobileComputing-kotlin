plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Ktor
    id("kotlinx-serialization")

    // Room DB
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.mangiaebasta"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mangiaebasta"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Location
    implementation(libs.kotlinx.coroutines.play.services)

    // Mapbox
    //implementation(libs.android)
    //implementation(libs.maps.compose)

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // Ktor
    implementation(libs.ktor.ktor.client.core)
    implementation(libs.ktor.client.android)
    // Logging
    implementation(libs.ktor.client.logging)
    // JSON Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.io.ktor.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json.v210)

    // DataStore for Preferences
    implementation(libs.androidx.datastore.preferences)

    // Room DB
    implementation(libs.androidx.room.runtime)

    // Pull Refresh
    implementation(libs.material3)

    //Icons
    implementation(libs.androidx.material.icons.extended)


}