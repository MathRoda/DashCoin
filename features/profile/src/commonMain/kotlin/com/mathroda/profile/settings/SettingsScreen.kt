package com.mathroda.profile.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mathroda.common.components.CommonTopBar
import com.mathroda.profile.settings.components.EnableNotifications
import com.mathroda.profile.settings.components.VersionItem

@Composable
fun SettingsScreen(
    viewModel: SettingViewModel,
    navigateBack: () -> Unit
) {
    val notificationsState by viewModel.enableNotificationsState.collectAsState()
    val appVersion = remember { viewModel.getAppVersion() }

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

            VersionItem(appVersion = appVersion)
        }
    }

    LaunchedEffect(true) {
        viewModel.init()
    }
}