// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${DaggerHilt.version}")
    }
}

plugins {
    id("com.android.application") version Build.gradlePluginVersion apply false
    id("com.android.library") version Build.gradlePluginVersion apply false
    kotlin("android") version "1.6.10" apply false
    kotlin("jvm") version Kotlin.version apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}