package com.mathroda.chart.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.testTag
import com.mathroda.chart.common.model.ChartDataSet
import com.mathroda.chart.common.internal.NO_SELECTION
import com.mathroda.chart.common.internal.TestTags
import com.mathroda.chart.common.internal.barstackedchart.generateColorShades
import com.mathroda.chart.common.internal.common.composable.ChartErrors
import com.mathroda.chart.common.internal.common.composable.ChartView
import com.mathroda.chart.common.internal.piechart.PieChart
import com.mathroda.chart.common.internal.validatePieData
import com.mathroda.chart.common.style.PieChartDefaults
import com.mathroda.chart.common.style.PieChartStyle

/**
 * A composable function that displays a Pie Chart.
 *
 * @param dataSet The data set to be displayed in the chart.
 * @param style The style to be applied to the chart. If not provided, the default style will be used.
 */
@Composable
fun PieChartView(
    dataSet: ChartDataSet,
    style: PieChartStyle = PieChartDefaults.style(),
) {
    style.pieColors = style.pieColors.ifEmpty {
        generateColorShades(style.pieColor, dataSet.data.item.points.size)
    }

    val errors by remember {
        mutableStateOf(validatePieData(dataSet = dataSet, style = style))
    }

    if (errors.isNotEmpty()) {
        ChartErrors(chartViewStyle = style.chartViewStyle, errors = errors)
    } else {
        ChartContent(dataSet = dataSet, style = style)
    }
}

@Composable
private fun ChartContent(
    dataSet: ChartDataSet,
    style: PieChartStyle
) {
    var title by remember { mutableStateOf(dataSet.data.label) }

    ChartView(chartViewsStyle = style.chartViewStyle) {
        Text(
            modifier = style.chartViewStyle.modifierTopTitle
                .testTag(TestTags.CHART_TITLE),
            text = title,
            style = style.chartViewStyle.styleTitle
        )
        PieChart(
            chartData = dataSet.data.item,
            style = style,
            chartStyle = style.chartViewStyle
        ) {
            title = when (it) {
                NO_SELECTION -> dataSet.data.label
                else -> dataSet.data.item.labels[it]
            }
        }
    }
}
