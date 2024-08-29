buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.3.15")
    }

}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.0" apply false
    id("com.android.library") version "8.5.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.22" apply false
    kotlin("plugin.serialization") version "1.9.20" apply false
    id("com.google.devtools.ksp") version "2.0.10-1.0.24" apply false
    id("androidx.room") version "2.7.0-alpha07" apply false
    //kotlin("plugin.serialization") version "1.9.2" apply false
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.layout.buildDirectory)
}