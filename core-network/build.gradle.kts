import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.mathroda.network"
    compileSdk = Configuration.compileSdk

    defaultConfig {
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk

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

    //retrofit
    implementation(Deps.SquareUp.Retrofit2.retrofit)
    implementation(Deps.SquareUp.Retrofit2.convertorGson)
    implementation(Deps.SquareUp.Okhhtp3.okhttp)

    // Dagger hilt
    implementation(Deps.Google.DaggerHilt.android)
    kapt(Deps.Google.DaggerHilt.compiler)
    kapt(Deps.AndroidX.Hilt.compiler)


}