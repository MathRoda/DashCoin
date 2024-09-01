package com.mathroda.common.util

import androidx.compose.ui.graphics.toArgb
import com.mathroda.common.theme.CustomGreen
import com.mathroda.common.theme.CustomRed
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

fun Double.getColorStatusToArgb() =
    if (this < 0) CustomRed.toArgb() else CustomGreen.toArgb()

fun Double.getColorStatus() =
    if (this < 0) CustomRed else CustomGreen


suspend fun <T, R> List<T>.asyncMap(
    transform: suspend (T) -> R
): List<R> = coroutineScope {
    map {
        async { transform(it) }.await()
    }
}


