plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    buildFeatures {
        viewBinding = true // 视图绑定，取代`kotlin-android-extensions`
        dataBinding = true // 数据绑定
    }

    defaultConfig {
        applicationId = "io.lzyprime.definitely"
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
}

dependencies {
    implementation(project(":core"))

    val navVersion: String by rootProject.extra
    val coilVersion: String by rootProject.extra
    val publicDependencies: List<String> by rootProject.extra
    val publicKapt: List<String> by rootProject.extra

    publicDependencies.forEach { implementation(it) }
    publicKapt.forEach { kapt(it) }


    // navigation
    implementation("androidx.navigation:navigation-runtime-ktx:$navVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // Coil图片展示
    implementation("io.coil-kt:coil:$coilVersion")

}