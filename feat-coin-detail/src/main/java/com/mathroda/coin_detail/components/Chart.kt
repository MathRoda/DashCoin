package com.mathroda.coin_detail.components

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.mathroda.coin_detail.utils.ChartViewState
import com.mathroda.coin_detail.utils.setLineDataSet
import com.mathroda.common.theme.TextWhite
import com.mathroda.domain.Charts

enum class TimeRange {
    ONE_DAY, ONE_WEEK, ONE_MONTH, ONE_YEAR, ALL
}

@Composable
fun Chart(
    oneDayChange: Double,
    context: Context,
    charts: Charts
) {

    val dataSet = mutableListOf<Entry>()
    charts.let { chartsValue ->
        chartsValue.chart?.onEach { value ->
            dataSet.add(addEntry(value[0], value[1]))
        }
    }

    AndroidView(
        factory = {
            LineChart(context)
        },
        update = { lineChart ->

            val lineDataSet =
                ChartViewState().getLineDataSet(
                    lineData = dataSet,
                    label = "chart values",
                    oneDayChange = oneDayChange,
                    context = context
                )


            lineChart.apply {
                description.isEnabled = false
                isDragEnabled = false
                xAxis.isEnabled = false
                axisLeft.setDrawAxisLine(false)
                axisLeft.textColor = TextWhite.toArgb()
                axisRight.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)
                setScaleEnabled(false)
                setDrawGridBackground(false)
                setDrawBorders(false)
                setLineDataSet(lineDataSet)
                invalidate()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(300.dp)
    )
}


fun addEntry(x: Float, y: Float) =
    Entry(x, y)