import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
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
            baseName = "core"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {

            with(Deps.Org.Jetbrains.Kotlinx) {
                implementation(dateTime)
            }

            //Koin
            implementation(Deps.Koin.core)
        }
    }
}

buildkonfig {
    packageName = "com.mathroda.core"

    // default config is required
    defaultConfigs {
        buildConfigField(FieldSpec.Type.INT, "VERSION_CODE", Configuration.versionCode.toString())
        buildConfigField(FieldSpec.Type.STRING, "VERSION_NAME", Configuration.versionName)
    }
}

android {
    namespace = "com.mathroda.core"
    compileSdk = Configuration.compileSdk

    defaultConfig { minSdk = Configuration.minSdk }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
