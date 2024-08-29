import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import com.mathroda.buildsrc.Deps.Org.Jetbrains.Kotlinx.coroutineCore

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("kotlin-kapt")
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
            export(Deps.KMPNotifier.notifications)
        }
    }
    sourceSets {
        androidMain.dependencies {
            //Work Manger
            implementation(Deps.AndroidX.Work.runtime)

            //Koin
            implementation(platform(Deps.Koin.bom))
            implementation(Deps.Koin.android)
            implementation(Deps.Koin.workManger)
        }

        val androidUnitTest by getting {
            dependencies {
            }
        }

        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":features:common"))
            implementation(project(":core-datasource"))
            implementation(project(":core-domain"))

            //Koin
            implementation(platform(Deps.Koin.bom))
            implementation(Deps.Koin.core)

            implementation(coroutineCore)

            //KMPNotifier
            api(Deps.KMPNotifier.notifications)

        }
    }

}
android {
    namespace = "com.mathroda.infrastructure"
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