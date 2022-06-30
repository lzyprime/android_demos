val navVersion: String by rootProject.extra
val hiltVersion: String by rootProject.extra
val useCompose: Boolean by rootProject.extra
val lifecycleVersion = "2.5.0"
val composeVersion = "1.1.1"
val activityVersion = "1.4.0"

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

if (!useCompose) {
    apply(plugin = "androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "io.lzyprime.definitely"
        minSdk = 29
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = !useCompose // 视图绑定，取代`kotlin-android-extensions`
//        dataBinding = !useCompose // 数据绑定
        compose = useCompose
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
        jvmTarget = "1.8"
    }

    if (useCompose) {
        composeOptions {
            kotlinCompilerExtensionVersion = composeVersion
        }
    }
    sourceSets {
        getByName("main") {
            if (useCompose) {
                kotlin.srcDir("src/ui/compose")
            } else {
                res.srcDir("src/ui/res")
                kotlin.srcDir("src/ui/view")
            }
        }
    }
}

dependencies {
    // for ktor server
    implementation(project(":svr"))

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")

    if (useCompose) {
        implementation("androidx.activity:activity-compose:$activityVersion")
        implementation("androidx.compose.material:material:$composeVersion")
        implementation("androidx.compose.animation:animation:$composeVersion")
        implementation("androidx.compose.ui:ui-tooling:$composeVersion")

        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

        implementation("androidx.navigation:navigation-compose:$navVersion")
    } else {
        implementation("com.google.android.material:material:1.6.1")
        implementation("androidx.activity:activity-ktx:$activityVersion")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")

        implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
        implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

        implementation("io.coil-kt:coil:2.1.0")
    }
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    // dataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
}