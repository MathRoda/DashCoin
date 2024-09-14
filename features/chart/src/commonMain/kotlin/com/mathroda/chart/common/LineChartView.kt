package com.mathroda.chart.common

import androidx.compose.runtime.Composable
import com.mathroda.chart.common.model.ChartDataSet
import com.mathroda.chart.common.model.MultiChartDataSet
import com.mathroda.chart.common.internal.common.model.MultiChartData
import com.mathroda.chart.common.internal.linechart.LineChartViewImpl
import com.mathroda.chart.common.style.LineChartDefaults
import com.mathroda.chart.common.style.LineChartStyle

/**
 * A composable function that displays a Line Chart for a single data set.
 *
 * @param dataSet The data set to be displayed in the chart.
 * @param style The style to be applied to the chart. If not provided, the default style will be used.
 */
@Composable
fun LineChartView(
    dataSet: ChartDataSet,
    style: LineChartStyle = LineChartDefaults.style()
) {
    LineChartViewImpl(
        data = MultiChartData(
            items = listOf(dataSet.data),
            title = dataSet.data.label
        ),
        style = style
    )
}

/**
 * A composable function that displays a Line Chart for multiple data sets.
 *
 * @param dataSet The data sets to be displayed in the chart.
 * @param style The style to be applied to the chart. If not provided, the default style will be used.
 */
@Composable
fun LineChartView(
    dataSet: MultiChartDataSet,
    style: LineChartStyle = LineChartDefaults.style()
) {
    LineChartViewImpl(
        data = dataSet.data,
        style = style
    )
}
