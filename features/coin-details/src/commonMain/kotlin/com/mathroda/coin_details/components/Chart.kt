package com.mathroda.coin_details.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.mathroda.chart.common.LineChartView
import com.mathroda.chart.common.model.ChartDataSet
import com.mathroda.chart.common.style.ChartViewDefaults
import com.mathroda.chart.common.style.LineChartDefaults
import com.mathroda.coin_details.Point
import com.mathroda.coin_details.state.ChartState
import com.mathroda.coin_details.utils.yAxisDrawing
import com.mathroda.common.theme.DarkGray
import com.mathroda.common.theme.TextWhite
import com.mathroda.common.util.getColorStatus

enum class TimeRange {
    ONE_DAY, ONE_WEEK, ONE_MONTH, ONE_YEAR, ALL
}

@Composable
fun Chart(
    oneDayChange: Double,
    charts: ChartState
) {
    val generalPadding = 24.dp
    val textMeasurer = rememberTextMeasurer()
    val points =  charts.chart
    val density = LocalDensity.current
    val upperValue by rememberSaveable {
        mutableDoubleStateOf(points.getUpperValue())
    }
    val lowerValue by rememberSaveable {
        mutableDoubleStateOf(points.getLowerValue())
    }
    val yAxisWidth by remember {
        derivedStateOf {
            val width = textMeasurer.measure("44.4K").size.width
            with(density) { width.toDp() }
        }
    }
    val dataSet = ChartDataSet(
        items = points.map { it.y },
        title = ""
    )

    val style = LineChartDefaults.style(
        lineColor = oneDayChange.getColorStatus(),
        pointVisible = false,
        pointColor = DarkGray,
        pointSize = 0f,
        bezier = true,
        dragPointColor = DarkGray,
        dragPointVisible = false,
        dragPointSize = 0f,
        dragActivePointSize = 0f,
        chartViewStyle = ChartViewDefaults.style(
            backgroundColor = DarkGray,
            outerPadding = generalPadding,
            innerPadding = 0.dp,
            shadow = 0.dp
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(300.dp)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.padding(start = yAxisWidth)) {
            LineChartView(
                dataSet = dataSet,
                style = style
            )
        }

        Canvas(
            modifier = Modifier
                .requiredWidth(yAxisWidth)
                .fillMaxHeight()
                .padding(vertical = generalPadding)
        ) {
            val spacingY = (size.height / 8.dp.toPx()).dp
            yAxisDrawing(
                upperValue = upperValue.toFloat(),
                lowerValue = lowerValue.toFloat(),
                textMeasure = textMeasurer,
                spacing = spacingY,
                yAxisStyle = TextStyle(
                    color = TextWhite
                ),
                yAxisRange = 4
            )
        }
    }

}

private fun List<Point>.getUpperValue(): Double {
    return this.maxOfOrNull { item -> item.y }?.plus(1.0) ?: 0.0
}

private fun List<Point>.getLowerValue(): Double {
    return this.minOfOrNull { item -> item.y }?.toDouble() ?: 0.0
}
