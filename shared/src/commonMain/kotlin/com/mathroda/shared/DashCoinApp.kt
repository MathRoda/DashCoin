@file:OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class, ExperimentalMaterialApi::class
)

package com.mathroda.shared

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mathroda.common.theme.DashCoinTheme
import com.mathroda.common.toastmessage.components.ContentWithMessageBar
import com.mathroda.common.toastmessage.components.LocalMessageBar
import com.mathroda.common.toastmessage.components.rememberMessageBarState
import com.mathroda.shared.destination.Destinations
import com.mathroda.shared.navigation.main.MainGraph
import com.mathroda.shared.navigation.main.MainScreen

@Composable
fun DashCoinApp() {
    DashCoinTheme {
        val navController = rememberNavController()
        ProvideMessageBar {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                MainScreen(
                    navController = navController,
                    startDestinations = Destinations.CoinsScreen //TODO: Implement conditional navigation in viewModel
                )
            }
        }
    }
}

@Composable
fun ProvideMessageBar(
    content: @Composable () -> Unit
) {
    val messageBarState = rememberMessageBarState()
    CompositionLocalProvider(
        LocalMessageBar provides messageBarState
    ) {
        ContentWithMessageBar(messageBarState = messageBarState) {
            content()
        }
    }
}