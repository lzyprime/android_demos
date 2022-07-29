buildscript {
    val navVersion by extra("2.5.1")
    val hiltVersion by extra("2.43")
    val useCompose by extra(false)
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
        if (!useCompose) {
            classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
