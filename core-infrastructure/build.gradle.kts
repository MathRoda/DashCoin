import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps
import com.mathroda.buildsrc.Deps.Org.Jetbrains.Kotlinx.coroutineCore
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
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
            baseName = "infrastructure"
            isStatic = true
            export(Deps.KMPNotifier.notifications)
        }
    }
    sourceSets {
        androidMain.dependencies {
            //Work Manger
            implementation(Deps.AndroidX.Work.runtime)

            //Koin
            implementation(Deps.Koin.android)
            implementation(Deps.Koin.workManger)
        }

        val androidUnitTest by getting {
            dependencies {
            }
        }

        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":features:common"))
            implementation(project(":core-datasource"))
            implementation(project(":core-domain"))

            //Koin
            implementation(Deps.Koin.core)

            implementation(coroutineCore)

            //KMPNotifier
            api(Deps.KMPNotifier.notifications)

            //Konnectivity
            implementation(Deps.Konnectivity.dep)

        }
    }

}
android {
    namespace = "com.mathroda.infrastructure"
    compileSdk = Configuration.compileSdk

    defaultConfig { minSdk = Configuration.minSdk }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}