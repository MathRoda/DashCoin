package com.mathroda.buildsrc

import com.mathroda.buildsrc.Version.androidx_lifecycle
import com.mathroda.buildsrc.Version.compose_navigation_version
import com.mathroda.buildsrc.Version.compose_version
import com.mathroda.buildsrc.Version.koin_version
import com.mathroda.buildsrc.Version.kotlinx_coroutines
import com.mathroda.buildsrc.Version.play_services_auth_version
import com.mathroda.buildsrc.Version.room_version
import com.mathroda.buildsrc.Version.sqlite_bundeled

object Deps {

    object AndroidX {
        object Core {
            const val coreKtx = "androidx.core:core-ktx:${Version.androidx_core}"
            const val splashScreen = "androidx.core:core-splashscreen:1.0.0"
        }
        object Compose {
            const val ui = "androidx.compose.ui:ui:$compose_version"
            const val material = "androidx.compose.material:material:$compose_version"
            const val material3 = "androidx.compose.material3:material3:1.2.1"
            const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$compose_version"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$compose_version"
            const val runtime = "androidx.compose.runtime:runtime-livedata:$compose_version"
            const val composeNavigation = "org.jetbrains.androidx.navigation:navigation-compose:${compose_navigation_version}"
            const val viewModelCompose = "org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:${Version.viewModelCompose}"
        }
        object Lifecycle {
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$androidx_lifecycle"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$androidx_lifecycle"
            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$androidx_lifecycle"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$androidx_lifecycle"
        }
        object Activity {
            const val compose = "androidx.activity:activity-compose:1.7.0"
        }
        object Navigation {
            const val compose = "androidx.navigation:navigation-compose:2.5.3"
        }
        object ConstraintLayout {
            const val compose = "androidx.constraintlayout:constraintlayout-compose:1.0.1"
        }
        object AppCompat {
            const val appcompat = "androidx.appcompat:appcompat:1.6.1"
        }
        object Room {
            const val runtime = "androidx.room:room-runtime:$room_version"
            const val compiler = "androidx.room:room-compiler:$room_version"
            const val ktx = "androidx.room:room-ktx:$room_version"
            const val sqlLite = "androidx.sqlite:sqlite-bundled:$sqlite_bundeled"
        }
        object Work {
            const val runtime = "androidx.work:work-runtime-ktx:2.8.1"
        }
        object Arch {
            const val coreTest = "androidx.arch.core:core-testing:2.2.0"
        }
        object Test {
            const val core = "androidx.test:core:1.5.0"
        }
        object DataStore {
            const val preferences = "androidx.datastore:datastore-preferences:1.1.0"
            const val dataStore = "androidx.datastore:datastore:1.1.0"
        }
    }

    object Google {
        object Accompanist {
            const val insetsUi = "com.google.accompanist:accompanist-insets-ui:0.23.1"
            const val flowLayout = "com.google.accompanist:accompanist-flowlayout:0.17.0"
            const val swipeRefresh = "com.google.accompanist:accompanist-swiperefresh:0.23.1"
            const val pager = "com.google.accompanist:accompanist-pager:0.26.4-beta"
            const val pager_indicator = "com.google.accompanist:accompanist-pager-indicators:0.26.4-beta"
        }

        object AndroidMaterial {
            const val material = "com.google.android.material:material:1.8.0"
        }
        object AndroidGms {
            const val playServicesAuth = "com.google.android.gms:play-services-auth:$play_services_auth_version"
        }
        object Firebase {
            const val authKtx = "dev.gitlive:firebase-auth:2.0.0"
            const val fireStoreKtx = "dev.gitlive:firebase-firestore:2.0.0"
            const val storage = "dev.gitlive:firebase-storage:2.0.0"
        }
        object Truth {
            const val truth = "com.google.truth:truth:1.1.3"
        }
    }

    object Org {
        object Jetbrains {
            object Kotlinx {
                const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines"
                const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinx_coroutines"
                const val coroutinePlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$kotlinx_coroutines"
                const val coroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4"
                const val kotlinxSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0"
                const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.6.1"
            }
        }
    }

    object GoogleServices {
        const val credential = "androidx.credentials:credentials:1.3.0-rc01"
        const val credPlayServices = "androidx.credentials:credentials-play-services-auth:1.3.0-rc01"
        const val identity = "com.google.android.libraries.identity.googleid:googleid:1.1.1"
    }

    object Junit {
        const val junit4 = "junit:junit:4.13.2"
    }

    object IO {
        object Coil {
            const val network = "io.coil-kt.coil3:coil-network-ktor:3.0.0-alpha10"
            const val compose = "io.coil-kt.coil3:coil-compose:3.0.0-alpha10"
        }
        object Mockk {
            const val mockk = "io.mockk:mockk:1.13.4"
        }
    }

    object SquareUp {
        object Okhhtp3 {
            const val mockwebserver = "com.squareup.okhttp3:mockwebserver:4.10.0"
            const val okhttp = "com.squareup.okhttp3:okhttp:5.0.0-alpha.2"
        }
    }

    object Github {
        object Tfaki {
            const val composableSweetToast = "com.github.tfaki:ComposableSweetToast:1.0.1"
        }
    }

    object Airbnb {
        object Android {
            const val lottieCompose = "com.airbnb.android:lottie-compose:5.2.0"
            const val kottie = "io.github.ismai117:kottie:2.0.0"
        }
    }

    object Kotlinx {
        object Collections {
            const val immuatble = "org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.6"
        }
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:$koin_version"
        const val android = "io.insert-koin:koin-android:$koin_version"
        const val compose = "io.insert-koin:koin-compose:$koin_version"
        const val workManger = "io.insert-koin:koin-androidx-workmanager:$koin_version"
        const val viewModel = "io.insert-koin:koin-compose-viewmodel:$koin_version"
    }

    object Ktor {
        const val ktorClientCore = "io.ktor:ktor-client-core:${Version.ktor}"
        const val ktorSerializationKotlinxJson = "io.ktor:ktor-serialization-kotlinx-json:${Version.ktor}"
        const val ktorClientContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Version.ktor}"
        const val ktorClientLogging = "io.ktor:ktor-client-logging:${Version.ktor}"
        const val ktorOkhttp = "io.ktor:ktor-client-okhttp:${Version.ktor}"
        const val ktorDarwin = "io.ktor:ktor-client-darwin:${Version.ktor}"
    }

    object Charts {
        const val lib = "io.github.dautovicharis:charts:1.2.0"
        const val snapshots = "io.github.dautovicharis:charts:2.0.0-SNAPSHOT"
    }

    object KMPNotifier {
        const val notifications = "io.github.mirzemehdi:kmpnotifier:1.2.1"
    }

    object Konnectivity {
        const val dep = "com.plusmobileapps:konnectivity:0.1-alpha01"
    }

    object Peekaboo {
        const val imagePicker = "io.github.onseok:peekaboo-image-picker:0.5.2"
    }

}