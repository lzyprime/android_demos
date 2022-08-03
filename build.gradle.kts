buildscript {
    val useCompose by extra(true)
    val navVersion by extra("2.5.1")
    val hiltVersion by extra("2.43.1")
    val kotlinVersion = if(useCompose) "1.7.0" else "1.7.10"
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
        if (!useCompose) {
            classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
