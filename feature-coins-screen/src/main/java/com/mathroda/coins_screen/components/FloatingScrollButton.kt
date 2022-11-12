package com.mathroda.coins_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mathroda.common.theme.LighterGray

@Composable
fun FloatingScrollButton(
    modifier: Modifier = Modifier,
    visibility: Boolean,
    onClick: () -> Unit,

    ) {

    AnimatedVisibility(
        visible = visibility,
        enter = EnterTransition.None,
        exit = ExitTransition.None
        ) {

        FloatingActionButton(
            onClick = { onClick() },
            modifier = modifier,
            backgroundColor = com.mathroda.common.theme.LighterGray,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = "Scroll Up"
            )

        }
    }

}