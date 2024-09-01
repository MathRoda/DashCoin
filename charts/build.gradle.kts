import com.mathroda.buildsrc.Configuration

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
    id("kotlin-kapt")
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
            baseName = "charts"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    compileSdk = Configuration.compileSdk
    namespace = "com.mathroda.coin_detail"

    defaultConfig { minSdk = Configuration.minSdk }
}