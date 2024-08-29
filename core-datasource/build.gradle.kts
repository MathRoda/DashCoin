import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.google.gms.google-services")
    kotlin("plugin.serialization")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }
    sourceSets {
        androidMain.dependencies {
            //Koin
            implementation(platform(Deps.Koin.bom))
            implementation(Deps.Koin.android)

            with(Deps.GoogleServices) {
                implementation(credential)
                implementation(credPlayServices)
                implementation(identity)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(Deps.AndroidX.Test.core)
                implementation(Deps.Junit.junit4)
                implementation(Deps.AndroidX.Arch.coreTest)
                implementation(Deps.Org.Jetbrains.Kotlinx.coroutineTest)
                implementation(Deps.Google.Truth.truth)
                implementation(Deps.SquareUp.Okhhtp3.mockwebserver)

                implementation(Deps.IO.Mockk.mockk)
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
            implementation(platform(Deps.Koin.bom))
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

            //data store
            implementation(Deps.AndroidX.DataStore.preferences)
            implementation(Deps.AndroidX.DataStore.dataStore)
        }
    }

}

android {
    namespace = "com.mathroda.datasource"
    compileSdk = Configuration.compileSdk

    defaultConfig {
        minSdk = Configuration.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {
        release {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                )
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

    }
}
