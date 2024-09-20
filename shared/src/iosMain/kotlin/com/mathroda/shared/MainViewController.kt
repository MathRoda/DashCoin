package com.mathroda.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

fun MainViewController() = ComposeUIViewController{
    DashCoinApp(
        modifier = Modifier
            .fillMaxSize()
    )
}

fun initFirebase() {
    Firebase.initialize()
}