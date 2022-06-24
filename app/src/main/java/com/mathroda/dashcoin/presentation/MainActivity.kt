package com.mathroda.dashcoin.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mathroda.dashcoin.presentation.coins_screen.viewmodel.CoinsViewModel
import com.mathroda.dashcoin.presentation.ui.theme.DashCoinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashCoinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TryGetCoins()
                }
            }
        }
    }

    @Composable
    fun TryGetCoins(
        viewModel: CoinsViewModel = hiltViewModel()
    ) {
        val state = viewModel.state.value
        Log.d("coins", "error " + state.error)
        Log.d("coins", "loading " + state.isLoading.toString())
        Log.d("coins", state.coins.toString())

    }
}
