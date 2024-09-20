package com.mathroda.coins.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Returns whether the lazy list is currently scrolling up.
 */
@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var lastValue by remember(this) {
        mutableStateOf(firstVisibleItemIndex to firstVisibleItemScrollOffset)
    }

    return remember(this) {
        derivedStateOf {
            val (previousIndex, previousScrollOffset) = lastValue
            if (previousIndex != firstVisibleItemIndex || previousIndex == 0) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
               lastValue = firstVisibleItemIndex to firstVisibleItemScrollOffset
            }
        }
    }.value
}
