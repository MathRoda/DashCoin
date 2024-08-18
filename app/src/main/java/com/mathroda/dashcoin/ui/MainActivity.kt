package com.mathroda.dashcoin.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.mathroda.common.theme.DashCoinTheme
import com.mathroda.dashcoin.navigation.main.MainScreen
import com.mathroda.dashcoin.splash.SplashViewModel
import com.mathroda.dashcoin.ui.screens.coinDetailsScreen
import com.mathroda.dashcoin.ui.screens.coinsScreen
import com.mathroda.dashcoin.ui.screens.favoriteCoinsScreen
import com.mathroda.dashcoin.ui.screens.forgotPasswordScreen
import com.mathroda.dashcoin.ui.screens.newsScreen
import com.mathroda.dashcoin.ui.screens.onboardingScreen
import com.mathroda.dashcoin.ui.screens.settingsScreen
import com.mathroda.dashcoin.ui.screens.signInScreen
import com.mathroda.dashcoin.ui.screens.signUpScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalLayoutApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }

        setContent {
            DashCoinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val startDestination by viewModel.startDestination
                    Log.d("mainActivity", startDestination.toString())
                    MainScreen(startDestinations = startDestination)
                }
            }
        }
    }
}


