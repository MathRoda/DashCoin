import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
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
            linkerOpts.add("-lsqlite3") // add sqlite
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":core-domain"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            //Voyager
            with(Deps.Voyager) {
                implementation(screenModel)
            }

            implementation(Deps.Airbnb.Android.kottie)
        }
    }
}


android {
    compileSdk = Configuration.compileSdk
    namespace = "com.mathroda.common"

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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = com.mathroda.buildsrc.Version.kotlin_compiler_extension
    }
    packaging {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
            add("META-INF/gradle/incremental.annotation.processors")
        }
    }
}

/*
dependencies {

    implementation(project(":core"))
    implementation(project(":core-domain"))

    with(Deps.AndroidX.Compose) {
        implementation(ui)
        implementation(material)
        implementation(toolingPreview)
        implementation(materialIconsExtended)
        implementation(runtime)
    }

    //implementation androidx.activity:activity-compose:1.7.0
    implementation(Deps.AndroidX.Core.coreKtx)


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


    //lottie
    implementation(Deps.Airbnb.Android.lottieCompose)

    //Voyager
    with(Deps.Voyager) {
        implementation(screenModel)
    }


}*/
