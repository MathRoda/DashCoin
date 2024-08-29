import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import java.util.Properties

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-kapt")
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
            implementation(platform(Deps.Koin.bom))
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

    defaultConfig {
        minSdk = Configuration.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //consumerProguardFiles = "consumer-rules.pro"


        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
    }

    buildFeatures {
        buildConfig = true
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