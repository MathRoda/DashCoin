package com.mathroda.dashcoin.presentation.onboarding.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.LighterGray
import com.mathroda.common.theme.TextWhite

@Composable
fun CustomBottomSection(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(9.dp)
    ) {
        FloatingActionButton(
            onClick = onButtonClick,
            backgroundColor = com.mathroda.common.theme.LighterGray,
            contentColor = com.mathroda.common.theme.TextWhite,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = "Next"
            )
        }
    }
}