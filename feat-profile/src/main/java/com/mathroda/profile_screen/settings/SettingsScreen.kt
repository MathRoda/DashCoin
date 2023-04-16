package com.mathroda.profile_screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mathroda.common.components.CommonTopBar
import com.mathroda.profile_screen.settings.components.EnableNotifications

@Composable
fun SettingsScreen() {

    val viewModel = hiltViewModel<SettingViewModel>()

    val notificationsState by viewModel.enableNotificationsState.collectAsState()

    Scaffold(
        topBar = { CommonTopBar(title = "Settings") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            EnableNotifications(
                state = notificationsState,
                onEnabledClick = viewModel::updateNotificationState
            )
        }
    }

    LaunchedEffect(true) {
        viewModel.init()
    }
}