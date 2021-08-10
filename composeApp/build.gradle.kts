plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}



android {
    compileSdk = 31

    buildFeatures {
        compose = true
    }

    defaultConfig {
        applicationId = "io.lzyprime.definitelyCompose"
        minSdk = 26
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    composeOptions {
        val composeVersion: String by rootProject.extra
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation(project(":core"))

    val activityVersion: String by rootProject.extra
    val composeVersion: String by rootProject.extra
    val coilVersion: String by rootProject.extra
    val publicDependencies: List<String> by rootProject.extra
    val publicKapt: List<String> by rootProject.extra

    publicDependencies.forEach { implementation(it) }
    publicKapt.forEach { kapt(it) }

    // compose
    implementation("androidx.activity:activity-compose:$activityVersion")
    implementation("com.google.android.material:compose-theme-adapter:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0-beta01")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha09")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha03")
    implementation("io.coil-kt:coil-compose:$coilVersion")
}