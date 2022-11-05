package com.mathroda.dashcoin.presentation.coin_detail.utils

import android.content.Context
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.mathroda.dashcoin.core.util.getBackgroundColor
import com.mathroda.dashcoin.core.util.getColorStatusToArgb

class ChartScreenViewState {

    fun getLineDataSet(
        lineData: List<Entry>,
        label: String,
        oneDayChange: Double,
        context: Context
    ) =
        LineDataSet(lineData, label).apply {
            mode = LineDataSet.Mode.STEPPED
            color = oneDayChange.getColorStatusToArgb()
            highLightColor = oneDayChange.getColorStatusToArgb()
            fillDrawable = oneDayChange.getBackgroundColor(context)
            lineWidth = 2f
            setDrawFilled(true)
            setDrawCircles(false)
        }


}