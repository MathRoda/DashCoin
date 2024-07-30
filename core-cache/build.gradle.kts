import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.mathroda.cache"
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

    implementation(Deps.AndroidX.Core.coreKtx)
    implementation(Deps.AndroidX.AppCompat.appcompat)
    implementation(Deps.Google.AndroidMaterial.material)
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation(Deps.Junit.junit4)

    // Dagger hilt
    with(Deps.Google.DaggerHilt){
        implementation(android)
        kapt(compiler)
    }
    kapt(Deps.AndroidX.Hilt.compiler)

    //Room Database
    implementation(Deps.AndroidX.Room.runtime)
    annotationProcessor(Deps.AndroidX.Room.compiler)
    kapt(Deps.AndroidX.Room.compiler)
    implementation(Deps.AndroidX.Room.ktx)

}