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
import com.mathroda.common.components.CommonTopBar
import com.mathroda.profile_screen.settings.components.EnableNotifications
import com.mathroda.profile_screen.settings.components.VersionItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    navigateBack: () -> Unit
) {

    val viewModel = koinViewModel<SettingViewModel>()

    val notificationsState by viewModel.enableNotificationsState.collectAsState()

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Settings",
                navigateEnabled = true,
                onNavigateBack = navigateBack
            )
        }
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

            VersionItem()
        }
    }

    LaunchedEffect(true) {
        viewModel.init()
    }
}