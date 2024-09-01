import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import com.mathroda.buildsrc.Version

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
            baseName = "coin_detail"
            isStatic = true
            linkerOpts.add("-lsqlite3") // add sqlite
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":core-domain"))
            implementation(project(":core-datasource"))
            implementation(project(":features:common"))
            implementation(project(":core-infrastructure"))
            implementation(project(":charts"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            //Koin
            implementation(platform(Deps.Koin.bom))
            implementation(Deps.Koin.compose)

            //Voyager
            with(Deps.Voyager) {
                implementation(screenModel)
            }

            //KotlinDateTime
            implementation(Deps.Org.Jetbrains.Kotlinx.dateTime)

            //coil
            implementation(Deps.IO.Coil.compose)
        }
    }
}

android {
    compileSdk = Configuration.compileSdk
    namespace = "com.mathroda.coin_detail"

    defaultConfig { minSdk = Configuration.minSdk }
}