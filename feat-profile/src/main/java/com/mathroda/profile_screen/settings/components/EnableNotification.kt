package com.mathroda.profile_screen.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


data class EnableNotificationsState(
    val isEnabled: Boolean = true,
    val title: String = "Notifications Enabled"
)

@Composable
internal fun EnableNotifications(
    state: EnableNotificationsState,
    onEnabledClick: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = state.title,
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

        Switch(
            checked = state.isEnabled,
            onCheckedChange = onEnabledClick
        )
    }
}
