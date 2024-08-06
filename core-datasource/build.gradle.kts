import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
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
    kotlinOptions {
        jvmTarget = "17"
    }
}


dependencies {

    implementation(project(":core"))
    implementation(project(":core-domain"))
    implementation(project(":core-network"))
    implementation(project(":core-cache"))

    implementation(Deps.AndroidX.Core.coreKtx)
    implementation(Deps.AndroidX.AppCompat.appcompat)

    //data store
    implementation(Deps.AndroidX.DataStore.preferences)


    //Koin
    implementation(platform(Deps.Koin.bom))
    implementation(Deps.Koin.core)
    implementation(Deps.Koin.android)

    //Firebase
    with(Deps.Google.Firebase) {
        implementation(platform(bom))
        implementation(authKtx)
        implementation(fireStoreKtx)
        implementation(storage)
    }
    //Play Services
    implementation(Deps.Org.Jetbrains.Kotlinx.coroutinePlayServices)
    //Play Services Auth
    implementation(Deps.Google.AndroidGms.playServicesAuth)


    // Coroutines
    with(Deps.Org.Jetbrains.Kotlinx) {
        implementation(coroutineCore)
        implementation(coroutineAndroid)
        implementation(coroutinePlayServices)
    }

    //Work Manger
    implementation(Deps.AndroidX.Work.runtime)

    //coil
    implementation(Deps.IO.Coil.compose)

    //Ktor
    with(Deps.Ktor) {
        implementation(ktorClientCore)
    }

    //Local unit tests
    testImplementation(Deps.AndroidX.Test.core)
    testImplementation(Deps.Junit.junit4)
    testImplementation(Deps.AndroidX.Arch.coreTest)
    testImplementation(Deps.Org.Jetbrains.Kotlinx.coroutineTest)
    testImplementation(Deps.Google.Truth.truth)
    testImplementation(Deps.SquareUp.Okhhtp3.mockwebserver)

    testImplementation(Deps.IO.Mockk.mockk)

}