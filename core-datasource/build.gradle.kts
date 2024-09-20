import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.google.gms.google-services")
    kotlin("plugin.serialization")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        androidTarget {
            // compilerOptions DSL: https://kotl.in/u1r8ln
            compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "datasource"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            //Koin
            implementation(Deps.Koin.android)

            with(Deps.GoogleServices) {
                implementation(credential)
                implementation(credPlayServices)
                implementation(identity)
            }
        }

        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":core-domain"))
            implementation(project(":core-network"))
            implementation(project(":core-cache"))

            //data store
            implementation(Deps.AndroidX.DataStore.preferences)


            //Koin
            implementation(Deps.Koin.core)

            //Firebase
            with(Deps.Google.Firebase) {
                implementation(authKtx)
                implementation(fireStoreKtx)
                implementation(storage)
            }

            with(Deps.Org.Jetbrains.Kotlinx) {
                implementation(dateTime)
            }

            // Coroutines
            with(Deps.Org.Jetbrains.Kotlinx) {
                implementation(coroutineCore)
            }
        }
    }

}

android {
    namespace = "com.mathroda.datasource"
    compileSdk = Configuration.compileSdk

    defaultConfig { minSdk = Configuration.minSdk }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
