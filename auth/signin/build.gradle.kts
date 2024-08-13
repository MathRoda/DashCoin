import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import com.mathroda.buildsrc.Version

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
}

android {
    compileSdk = Configuration.compileSdk
    namespace = "com.mathroda.signin_screen"

    defaultConfig {
        minSdk = Configuration.minSdk

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
        kotlinCompilerExtensionVersion = Version.kotlin_compiler_extension
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
    implementation(project(":core-datasource"))
    implementation(project(":features:common"))

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

    // Coroutine Lifecycle Scopes
    with(Deps.AndroidX.Lifecycle) {
        implementation(viewModelKtx)
        implementation(runtimeKtx)
        implementation(viewModelCompose)
    }


    //SweetToast
    implementation(Deps.Github.Tfaki.composableSweetToast)

    //firebase
    implementation(Deps.Google.Firebase.authKtx)
    //Play Services
    implementation(Deps.Org.Jetbrains.Kotlinx.coroutinePlayServices)
    //Play Services Auth
    implementation(Deps.Google.AndroidGms.playServicesAuth)

    //Koin
    implementation(platform(Deps.Koin.bom))
    implementation(Deps.Koin.compose)
}