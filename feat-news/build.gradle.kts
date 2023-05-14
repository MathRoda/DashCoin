import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import com.mathroda.buildsrc.Version

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = Configuration.compileSdk
    namespace = "com.mathroda.news_screen"

    defaultConfig {
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //consumerProguardFiles "consumer-rules.pro"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose_version
    }
    packaging {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
            add("META-INF/gradle/incremental.annotation.processors")
        }
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":core-domain"))
    implementation(project(":core-datasource"))
    implementation(project(":feat-common"))

    with(Deps.AndroidX.Compose) {
        implementation(ui)
        implementation(material)
        implementation(toolingPreview)
        implementation(materialIconsExtended)
        implementation(runtime)
    }

    //implementation androidx.activity:activity-compose:1.7.0
    implementation(Deps.AndroidX.Core.coreKtx)
    implementation(Deps.AndroidX.Lifecycle.runtime)



    // Compose dependencies
    implementation(Deps.Google.Accompanist.flowLayout)
    implementation(Deps.AndroidX.Navigation.compose)
    //implementation "androidx.constraintlayout:constraintlayout-compose:1.0.1"


    //coil
    implementation(Deps.IO.Coil.compose)

    // Dagger hilt
    with(Deps.Google.DaggerHilt) {
        implementation(android)
        kapt(compiler)
    }
    kapt(Deps.AndroidX.Hilt.compiler)
    implementation(Deps.AndroidX.Hilt.navigationCompose)

    //lottie
    implementation(Deps.Airbnb.Android.lottieCompose)

    // Coroutine Lifecycle Scopes
    with(Deps.AndroidX.Lifecycle) {
        implementation(viewModelKtx)
        implementation(runtimeKtx)
        implementation(viewModelCompose)
    }

    //SweetToast
    implementation(Deps.Github.Tfaki.composableSweetToast)

    //SwipeToRefresh
    implementation(Deps.Google.Accompanist.swipeRefresh)
}