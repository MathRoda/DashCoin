package com.mathroda.chart.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.testTag
import com.mathroda.chart.common.internal.NO_SELECTION
import com.mathroda.chart.common.internal.TestTags
import com.mathroda.chart.common.internal.barchart.BarChart
import com.mathroda.chart.common.internal.common.composable.ChartErrors
import com.mathroda.chart.common.internal.common.composable.ChartView
import com.mathroda.chart.common.internal.validateBarData
import com.mathroda.chart.common.model.ChartDataSet
import com.mathroda.chart.common.style.BarChartDefaults
import com.mathroda.chart.common.style.BarChartStyle

/**
 * A composable function that displays a Bar Chart.
 *
 * @param dataSet The data set to be displayed in the chart.
 * @param style The style to be applied to the chart. If not provided, the default style will be used.
 */
@Composable
fun BarChartView(
    dataSet: ChartDataSet,
    style: BarChartStyle = BarChartDefaults.style()
) {

    val errors by remember {
        mutableStateOf(
            validateBarData(
                data = dataSet.data.item
            )
        )
    }

    if (errors.isEmpty()) {
        ChartContent(dataSet = dataSet, style = style)
    } else {
        ChartErrors(chartViewStyle = style.chartViewStyle, errors = errors)
    }
}

@Composable
private fun ChartContent(
    dataSet: ChartDataSet,
    style: BarChartStyle
) {
    var title by remember { mutableStateOf(dataSet.data.label) }
    ChartView(chartViewsStyle = style.chartViewStyle) {
        Text(
            modifier = style.chartViewStyle.modifierTopTitle
                .testTag(TestTags.CHART_TITLE),
            text = title,
            style = style.chartViewStyle.styleTitle
        )

        BarChart(chartData = dataSet.data.item, style = style) {
            title = when (it) {
                NO_SELECTION -> dataSet.data.label
                else -> dataSet.data.item.labels[it]
            }
        }
    }
}
