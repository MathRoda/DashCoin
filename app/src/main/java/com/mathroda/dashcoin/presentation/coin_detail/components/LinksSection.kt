package com.mathroda.dashcoin.presentation.coin_detail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LinkButton(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            color = Color.White
        )
    }
}