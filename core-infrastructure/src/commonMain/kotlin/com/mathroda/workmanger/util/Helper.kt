package com.mathroda.workmanger.util

internal fun Double.is5PercentUp(): Boolean {
    return this >= 5
}

internal fun Double.is5PercentDown(): Boolean {
    return this <= -5
}