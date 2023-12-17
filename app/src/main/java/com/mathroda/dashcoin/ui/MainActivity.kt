package com.mathroda.dashcoin.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.mathroda.common.components.InternetConnectivitySnackBar
import com.mathroda.common.theme.DashCoinTheme
import com.mathroda.core.state.InternetState
import com.mathroda.dashcoin.navigation.root.RootNavigationGraph
import com.mathroda.dashcoin.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalLayoutApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }

        setContent {
            DashCoinTheme {
                val internetState by viewModel.internetConnectivityState.collectAsState(
                    initial = InternetState.IDLE
                )
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ) {
                    InternetConnectivitySnackBar(internetState)
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        val startDestination by viewModel.startDestination
                        Log.d("mainActivity", startDestination)

                        RootNavigationGraph(
                            navHostController = rememberNavController(),
                            startDestination = startDestination,
                        )
                    }
                }
            }
        }
    }
}


