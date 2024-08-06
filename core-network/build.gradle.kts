import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    kotlin("plugin.serialization")
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
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":core-domain"))


    implementation(Deps.AndroidX.Core.coreKtx)
    implementation(Deps.AndroidX.AppCompat.appcompat)
    implementation(Deps.Google.AndroidMaterial.material)
    testImplementation(Deps.Junit.junit4)


    //Koin
    implementation(platform(Deps.Koin.bom))
    implementation(Deps.Koin.core)

    //Ktor
    with(Deps.Ktor) {
        implementation(ktorClientCore)
        implementation(ktorSerializationKotlinxJson)
        implementation(ktorClientContentNegotiation)
        implementation(ktorClientLogging)
        implementation(ktorOkhttp)
    }

    implementation(Deps.Org.Jetbrains.Kotlinx.kotlinxSerializationJson)

}