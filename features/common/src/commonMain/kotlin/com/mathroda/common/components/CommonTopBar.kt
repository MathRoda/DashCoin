package com.mathroda.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.TextWhite

@Composable
fun CommonTopBar(
    modifier: Modifier = Modifier,
    title: String,
    navigateEnabled: Boolean = false,
    onNavigateBack: () -> Unit = {}
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (navigateEnabled) {
                BackStackButton(
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    onNavigateBack()
                }
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h2,
                color = TextWhite,
            )
        }
    }
}