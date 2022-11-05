package com.mathroda.dashcoin.core.util

import android.content.Context
import androidx.compose.ui.graphics.toArgb
import com.mathroda.dashcoin.R
import com.mathroda.dashcoin.data.dto.Coin
import com.mathroda.dashcoin.presentation.coin_detail.utils.getCompatDrawable
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen
import com.mathroda.dashcoin.presentation.ui.theme.CustomRed

fun Double.getColorStatusToArgb() =
    if (this < 0) CustomRed.toArgb() else CustomGreen.toArgb()

fun Double.getColorStatus() =
    if (this < 0) CustomRed else CustomGreen


fun Double.getBackgroundColor(context: Context) =
    if (this < 0) {
        context.getCompatDrawable(R.drawable.background_negative_chart)
    } else context.getCompatDrawable(R.drawable.background_positive_chart)