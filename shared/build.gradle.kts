import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
    id("com.codingfeline.buildkonfig")
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
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":core-domain"))
            implementation(project(":core-cache"))
            implementation(project(":core-network"))
            implementation(project(":core-datasource"))
            implementation(project(":core-infrastructure"))
            implementation(project(":features:common"))
            implementation(project(":features:coin-details"))
            implementation(project(":features:coins"))
            implementation(project(":features:favorite-coins"))
            implementation(project(":features:news"))
            implementation(project(":features:profile"))
            implementation(project(":features:onboarding"))
            implementation(project(":auth:signin"))
            implementation(project(":auth:signup"))
            implementation(project(":auth:forgot-password"))
            //put your multiplatform dependencies here

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            //Koin
            implementation(Deps.Koin.core)
            implementation(Deps.Koin.viewModel)


            //Compose Navigation
            with(Deps.AndroidX.Compose) {
                implementation(composeNavigation)
            }

            //Firebase
            with(Deps.Google.Firebase) {
                implementation(authKtx)
                implementation(fireStoreKtx)
                implementation(storage)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.mathroda.shared.resources"
    generateResClass = always
}


buildkonfig {
    packageName = "com.mathroda.shared"

    // default config is required
    defaultConfigs {
        buildConfigField(FieldSpec.Type.INT, "VERSION_CODE", Configuration.versionCode.toString())
        buildConfigField(FieldSpec.Type.STRING, "VERSION_NAME", Configuration.versionName)
    }
}

android {
    namespace = "com.mathroda.shared"
    compileSdk = Configuration.compileSdk
    defaultConfig {
        minSdk = Configuration.minSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.withType<KotlinNativeLink> {
    kotlinOptions.freeCompilerArgs += "-Xoverride-konan-properties=osVersionMin.ios_simulator_arm64=15.0"
}