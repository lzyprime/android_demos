val navVersion: String by rootProject.extra
val hiltVersion: String by rootProject.extra
val useCompose: Boolean by rootProject.extra
val lifecycleVersion: String by rootProject.extra
val composeVersion: String by rootProject.extra
val activityVersion: String by rootProject.extra

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
    compileSdk = 33

    defaultConfig {
        applicationId = "io.lzyprime.definitely"
        minSdk = 29
        targetSdk = 33
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
//        dataBinding = true // 数据绑定
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
                kotlin.srcDir("src/compose/kotlin")
                res.srcDir("src/compose/res")
            } else {
                kotlin.srcDir("src/view/kotlin")
                res.srcDir("src/view/res")
            }
        }
    }
}

dependencies {
    // for ktor server
    implementation(project(":svr"))

    implementation("androidx.core:core-ktx:1.8.0")
    // hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    // For instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-compiler:$hiltVersion")
    // For local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptTest("com.google.dagger:hilt-compiler:$hiltVersion")

    if (useCompose) {
        implementation("androidx.activity:activity-compose:$activityVersion")
        implementation("androidx.compose.animation:animation:$composeVersion")
        implementation("androidx.compose.ui:ui-tooling:$composeVersion")
        // material
//        implementation("androidx.compose.material:material:$composeVersion")
        implementation("androidx.compose.material3:material3:1.0.0-alpha16")
        implementation("androidx.compose.material3:material3-window-size-class:1.0.0-alpha16")

        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

        implementation("androidx.navigation:navigation-compose:$navVersion")
    } else {
        implementation("androidx.appcompat:appcompat:1.5.0")
        implementation("androidx.activity:activity-ktx:$activityVersion")
        implementation("com.google.android.material:material:1.6.1")
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
}