val navVersion: String by rootProject.extra
val hiltVersion: String by rootProject.extra
val useCompose: Boolean by rootProject.extra
val lifecycleVersion = "2.5.1"
val composeVersion = "1.2.0"
val activityVersion = "1.5.1"

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
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
        if (useCompose) {
            vectorDrawables {
                useSupportLibrary = true
            }
        }
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
        packagingOptions {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

    sourceSets {
        getByName("main") {
            if (useCompose) {
                kotlin.srcDir("src/ui/compose")
                res.srcDir("src/ui/compose/res")
            } else {
                res.srcDir("src/ui/view/res")
                kotlin.srcDir("src/ui/view")
            }
        }
    }
}

dependencies {
    // for ktor server
    implementation(project(":svr"))

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("com.google.android.material:material:1.6.1")

    if (useCompose) {
        implementation("androidx.activity:activity-compose:$activityVersion")
        implementation("androidx.compose.animation:animation:$composeVersion")
        implementation("androidx.compose.ui:ui-tooling:$composeVersion")
        // material
//        implementation("androidx.compose.material:material:$composeVersion")
        implementation("androidx.compose.material3:material3:1.0.0-alpha15")
        implementation("androidx.compose.material3:material3-window-size-class:1.0.0-alpha15")

        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

        implementation("androidx.navigation:navigation-compose:$navVersion")
    } else {
        implementation("androidx.appcompat:appcompat:1.4.2")
        implementation("androidx.activity:activity-ktx:$activityVersion")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")

        implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
        implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
        // ViewModel
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
        implementation("io.coil-kt:coil:2.1.0")
    }
    // dataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
}