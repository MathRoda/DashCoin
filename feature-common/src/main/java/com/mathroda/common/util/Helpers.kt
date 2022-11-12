package com.mathroda.common.util

import android.content.Context
import androidx.compose.ui.graphics.toArgb
import com.mathroda.common.R
import com.mathroda.common.theme.CustomGreen
import com.mathroda.common.theme.CustomRed
import java.text.NumberFormat
import java.util.*

fun Double.getColorStatusToArgb() =
    if (this < 0) CustomRed.toArgb() else CustomGreen.toArgb()

fun Double.getColorStatus() =
    if (this < 0) CustomRed else CustomGreen


fun Double.getBackgroundColor(context: Context) =
    if (this < 0) {
        context.getCompatDrawable(R.drawable.background_negative_chart)
    } else context.getCompatDrawable(R.drawable.background_positive_chart)

fun numbersToCurrency(number: Int): String? {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.currency = Currency.getInstance("USD")
    return numberFormat.format(number)
}

fun numbersToFormat(number: Int): String? {
    val numberFormat = NumberFormat.getNumberInstance()
    return numberFormat.format(number)
}
