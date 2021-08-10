// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val gradleVersion by extra("7.0.2")
    val kotlinVersion by extra("1.5.21")
    val hiltVersion by extra("2.38.1")
    val navVersion by extra("2.3.5")
    val activityVersion by extra("1.3.1")
    val lifecycleVersion by extra("2.3.1")
    val imSDKVersion by extra("5.6.1200")
    extra["retrofitVersion"] = "2.9.0"
    extra["composeVersion"] = "1.0.3"
    extra["coilVersion"] = "1.3.2"
    val dataStoreVersion by extra("1.0.0")
    val pagingVersion by extra("3.0.1")

    extra["publicDependencies"] = listOf(
        "androidx.core:core-ktx:1.6.0",
        "com.google.android.material:material:1.4.0",
        "androidx.activity:activity-ktx:$activityVersion",
        "androidx.datastore:datastore-preferences:$dataStoreVersion",
        "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion",
        "com.google.dagger:hilt-android:$hiltVersion",
        "androidx.paging:paging-runtime:$pagingVersion",
        "com.tencent.imsdk:imsdk-plus:$imSDKVersion",
    )

    extra["publicKapt"] = listOf(
        "com.google.dagger:hilt-android-compiler:$hiltVersion",
    )

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
