import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
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
        }
    }

    sourceSets {
        commonMain.dependencies {
            //Voyager
            with(Deps.Voyager) {
                implementation(screenModel)
            }

            with(Deps.Org.Jetbrains.Kotlinx) {
                implementation(dateTime)
            }

        }
    }

}

android {
    namespace = "com.mathroda.core"
    compileSdk = Configuration.compileSdk
    defaultConfig {
        minSdk = Configuration.minSdk
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
