buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.3.15")
    }

}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.0" apply false
    id("com.android.library") version "8.0.0" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}