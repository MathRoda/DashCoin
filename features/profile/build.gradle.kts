import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import com.mathroda.buildsrc.Version
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
            baseName = "profile_screen"
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

            //Voyager
            with(Deps.Voyager) {
                implementation(screenModel)
            }

            //KotlinDateTime
            implementation(Deps.Org.Jetbrains.Kotlinx.dateTime)

            //Peekaboo
            implementation(Deps.Peekaboo.imagePicker)

        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.mathroda.profile_screen.resources"
    generateResClass = always
}

android {
    compileSdk = Configuration.compileSdk
    namespace = "com.mathroda.profile_screen"

    defaultConfig { minSdk = Configuration.minSdk }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

