import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.google.devtools.ksp")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        androidTarget {
            // compilerOptions DSL: https://kotl.in/u1r8ln
            compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "core_cache"
            isStatic = true
            linkerOpts.add("-lsqlite3") // add sqlite
        }
    }

    sourceSets.iosMain {
        kotlin.srcDirs("build/generated/ksp/metadata")
    }

    sourceSets {
        androidMain.dependencies {
            //Koin
            implementation(Deps.Koin.android)

        }

        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":core-domain"))

            //Room Database
            implementation(Deps.AndroidX.Room.runtime)
            implementation(Deps.AndroidX.Room.sqlLite)

            //Koin
            implementation(Deps.Koin.core)

            with(Deps.Org.Jetbrains.Kotlinx) {
                implementation(dateTime)
            }

            //data store
            implementation(Deps.AndroidX.DataStore.preferences)
            implementation(Deps.AndroidX.DataStore.dataStore)
        }

    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        // Common compiler options applied to all Kotlin source sets
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

}

android {
    namespace = "com.mathroda.cache"
    compileSdk = Configuration.compileSdk

    defaultConfig { minSdk = Configuration.minSdk }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    add("kspAndroid", Deps.AndroidX.Room.compiler)
    add("kspIosSimulatorArm64", Deps.AndroidX.Room.compiler)
    add("kspIosX64", Deps.AndroidX.Room.compiler)
    add("kspIosArm64", Deps.AndroidX.Room.compiler)
}



ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}