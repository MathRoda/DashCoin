package com.mathroda.coin_detail.utils

import android.content.Context
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.mathroda.common.util.getBackgroundColor
import com.mathroda.common.util.getColorStatusToArgb

class ChartViewState {



    fun getLineDataSet(
        lineData: List<Entry>,
        label: String,
        oneDayChange: Double,
        context: Context
    ) =
        LineDataSet(lineData, label).apply {
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            color = oneDayChange.getColorStatusToArgb()
            highLightColor = oneDayChange.getColorStatusToArgb()
            fillDrawable = oneDayChange.getBackgroundColor(context)
            lineWidth = 2f
            setDrawFilled(true)
            setDrawCircles(false)
        }


}