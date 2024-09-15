buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.3.15")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.15.2")
    }

}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false
    id("org.jetbrains.compose") version "1.7.0-beta02" apply false
    kotlin("jvm") version "2.0.20" apply false
    kotlin("multiplatform") version "2.0.20" apply  false
    kotlin("plugin.serialization") version "2.0.20" apply false
    id("com.google.devtools.ksp") version "2.0.20-1.0.24" apply false
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs = listOf(
            "-Xexpect-actual-classes", // Ignore expect/actual experimental state
        )
    }
}
