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


    // Coroutines
    with(Deps.Org.Jetbrains.Kotlinx) {
        implementation(coroutineCore)
        implementation(coroutineAndroid)
        implementation(coroutinePlayServices)
    }

    //Work Manger
    implementation(Deps.AndroidX.Work.runtime)

    //Koin
    implementation(platform(Deps.Koin.bom))
    implementation(Deps.Koin.core)
    implementation(Deps.Koin.android)
    implementation(Deps.Koin.workManger)

}