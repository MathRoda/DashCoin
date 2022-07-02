package com.mathroda.dashcoin.presentation.coin_detail.utils

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getCompatDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)