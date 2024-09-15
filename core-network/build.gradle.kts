import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-kapt")
    kotlin("plugin.serialization")
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
            baseName = "network"
            isStatic = true
        }
    }


    sourceSets {
        androidMain.dependencies {
            implementation(Deps.Ktor.ktorOkhttp)
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(Deps.Junit.junit4)
                implementation("androidx.test.ext:junit-ktx:1.1.5")
            }
        }

        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":core-domain"))

            //Koin
            implementation(Deps.Koin.core)

            //Ktor
            with(Deps.Ktor) {
                implementation(ktorClientCore)
                implementation(ktorSerializationKotlinxJson)
                implementation(ktorClientContentNegotiation)
                implementation(ktorClientLogging)
            }

            implementation(Deps.Org.Jetbrains.Kotlinx.kotlinxSerializationJson)

        }

        iosMain.dependencies {
            implementation(Deps.Ktor.ktorDarwin)
        }
    }

}

android {
    namespace = "com.mathroda.network"
    compileSdk = Configuration.compileSdk

    defaultConfig { minSdk = Configuration.minSdk }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}