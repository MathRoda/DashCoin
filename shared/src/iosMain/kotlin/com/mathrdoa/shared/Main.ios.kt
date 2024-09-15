package com.mathrdoa.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.mathroda.shared.DashCoinApp

@Composable
fun MainViewController() = ComposeUIViewController {
    DashCoinApp()
}