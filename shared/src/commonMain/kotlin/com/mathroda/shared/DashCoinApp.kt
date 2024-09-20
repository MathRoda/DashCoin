@file:OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class, ExperimentalMaterialApi::class
)

package com.mathroda.shared

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mathroda.common.theme.DashCoinTheme
import com.mathroda.common.toastmessage.components.ContentWithMessageBar
import com.mathroda.common.toastmessage.components.LocalMessageBar
import com.mathroda.common.toastmessage.components.rememberMessageBarState
import com.mathroda.shared.navigation.main.App
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashCoinApp(
    modifier: Modifier = Modifier
) {
    val viewModel: SharedViewModel = koinViewModel()
    val startDestinations by viewModel.startDestinations.collectAsState()

    DashCoinTheme {
        val navController = rememberNavController()
        Surface(
            color = MaterialTheme.colors.background
        ) {
            ProvideMessageBar(
                modifier = modifier.safeDrawingPadding()
            ) {
                App(
                    navController = navController,
                    startDestinations = startDestinations
                )
            }
        }
    }
}

@Composable
fun ProvideMessageBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val messageBarState = rememberMessageBarState()
    CompositionLocalProvider(
        LocalMessageBar provides messageBarState
    ) {
        ContentWithMessageBar(
            modifier = modifier,
            messageBarState = messageBarState
        ) {
            content()
        }
    }
}