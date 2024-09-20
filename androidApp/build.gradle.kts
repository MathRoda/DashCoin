import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import com.mathroda.buildsrc.Version

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    //id 'org.jetbrains.kotlin.plugin.serialization'
}

android {
    compileSdk = Configuration.compileSdk
    namespace = "com.mathroda.dashcoin"

    defaultConfig {
        applicationId = Configuration.applicationId
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName

        testInstrumentationRunner = "com.mathroda.dashcoin.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            isDebuggable = true
        }
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
    implementation(project(":core-infrastructure"))
    implementation(project(":shared"))

    with(Deps.AndroidX.Core){
        implementation(coreKtx)
        implementation(splashScreen)
    }
    with(Deps.AndroidX.Compose) {
        implementation(ui)
        implementation(material)
        implementation(toolingPreview)
        implementation(materialIconsExtended)
        implementation(runtime)
        implementation(composeNavigation)
    }
    // Coroutine Lifecycle Scopes
    with(Deps.AndroidX.Lifecycle) {
        implementation(viewModelKtx)
        implementation(runtimeKtx)
        implementation(viewModelCompose)
        implementation(runtime)
    }
    implementation(Deps.AndroidX.Activity.compose)

    // Compose dependencies
    implementation(Deps.AndroidX.Navigation.compose)
    implementation(Deps.AndroidX.ConstraintLayout.compose)

    // Accompanist insets UI
    with(Deps.Google.Accompanist) {
        implementation(insetsUi)
        implementation(flowLayout)
    }

    // Coroutines
    with(Deps.Org.Jetbrains.Kotlinx) {
        implementation(coroutineCore)
        implementation(coroutineAndroid)
        implementation(coroutinePlayServices)
    }

    implementation(Deps.Koin.android)
    implementation(Deps.Koin.workManger)

}
