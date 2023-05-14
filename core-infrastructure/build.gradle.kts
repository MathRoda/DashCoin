import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.mathroda.infrastructure"
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
    implementation(project(":feat-common"))
    implementation(project(":core-datasource"))
    implementation(project(":core-domain"))

    implementation(Deps.AndroidX.Core.coreKtx)
    implementation(Deps.AndroidX.AppCompat.appcompat)

    // Dagger hilt
    with(Deps.Google.DaggerHilt){
        implementation(android)
        implementation(compiler)
    }
    kapt(Deps.AndroidX.Hilt.compiler)
    implementation(Deps.AndroidX.Hilt.work)


    // Coroutines
    with(Deps.Org.Jetbrains.Kotlinx) {
        implementation(coroutineCore)
        implementation(coroutineAndroid)
        implementation(coroutinePlayServices)
    }

    //Work Manger
    implementation(Deps.AndroidX.Work.runtime)

}