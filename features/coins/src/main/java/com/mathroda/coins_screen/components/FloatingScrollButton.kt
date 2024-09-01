package com.mathroda.coins_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.LighterGray
import kotlinx.coroutines.launch


@Composable
fun ScrollButton(
    lazyListState: LazyListState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        val scope = rememberCoroutineScope()
        val firstVisibleItem = remember {
            derivedStateOf { lazyListState.firstVisibleItemIndex }
        }
        val isScrollingUp = lazyListState.isScrollingUp()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            FloatingScrollButton(
                modifier = Modifier
                    .padding(bottom = 64.dp, end = 16.dp),
                visibility = if (firstVisibleItem.value <= 4) false else isScrollingUp
            ) {
                scope.launch {
                    /**
                     * Scroll to first item in the list
                     */
                    lazyListState.animateScrollToItem(0)
                }
            }

        }
    }
}

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
            backgroundColor = LighterGray,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = "Scroll Up"
            )

        }
    }

}