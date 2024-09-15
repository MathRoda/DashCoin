import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        androidTarget {
            // compilerOptions DSL: https://kotl.in/u1r8ln
            compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "coins"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(Deps.Ktor.ktorOkhttp)
        }

        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":core-domain"))
            implementation(project(":core-datasource"))
            implementation(project(":features:common"))
            implementation(project(":features:profile"))
            implementation(project(":core-infrastructure"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            //Koin
            implementation(Deps.Koin.core)


            //KotlinDateTime
            implementation(Deps.Org.Jetbrains.Kotlinx.dateTime)

            //kotlinx
            implementation(Deps.Kotlinx.Collections.immuatble)

            //SwipeToRefresh
            implementation(Deps.Google.Accompanist.swipeRefresh)

            with(Deps.AndroidX.Compose) {
                implementation(viewModelCompose)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.mathroda.coins_screen.resources"
    generateResClass = always
}

android {
    compileSdk = Configuration.compileSdk
    namespace = "com.mathroda.coins_screen"

    defaultConfig { minSdk = Configuration.minSdk }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}