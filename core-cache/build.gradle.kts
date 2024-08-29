import com.mathroda.buildsrc.Configuration
import com.mathroda.buildsrc.Deps

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("androidx.room")
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
        androidMain.dependencies {
            //Koin
            implementation(platform(Deps.Koin.bom))
            implementation(Deps.Koin.android)

        }

        val androidUnitTest by getting {
            dependencies {
                implementation(Deps.Junit.junit4)
                implementation("androidx.test.ext:junit-ktx:1.1.5")
            }
        }

        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":core-domain"))

            //Room Database
            implementation(Deps.AndroidX.Room.runtime)
            implementation(Deps.AndroidX.Room.sqlLite)

            //Koin
            implementation(platform(Deps.Koin.bom))
            implementation(Deps.Koin.core)

            with(Deps.Org.Jetbrains.Kotlinx) {
                implementation(dateTime)
            }
        }
    }

}

android {
    namespace = "com.mathroda.cache"
    compileSdk = Configuration.compileSdk

    defaultConfig {
        minSdk = Configuration.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {
        release {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    add("kspAndroid", Deps.AndroidX.Room.compiler)
    add("kspIosSimulatorArm64", Deps.AndroidX.Room.compiler)
    add("kspIosX64", Deps.AndroidX.Room.compiler)
    add("kspIosArm64", Deps.AndroidX.Room.compiler)
}


room {
    schemaDirectory("$projectDir/schemas")
}