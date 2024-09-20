package com.mathroda.chart.common.internal.linechart

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.testTag
import com.mathroda.chart.common.internal.NO_SELECTION
import com.mathroda.chart.common.internal.TestTags
import com.mathroda.chart.common.internal.barstackedchart.LegendItem
import com.mathroda.chart.common.internal.barstackedchart.generateColorShades
import com.mathroda.chart.common.internal.common.composable.ChartErrors
import com.mathroda.chart.common.internal.common.composable.ChartView
import com.mathroda.chart.common.internal.common.model.MultiChartData
import com.mathroda.chart.common.internal.validateLineData
import com.mathroda.chart.common.style.LineChartDefaults
import com.mathroda.chart.common.style.LineChartStyle

@Composable
internal fun LineChartViewImpl(
    data: MultiChartData,
    style: LineChartStyle = LineChartDefaults.style(),
) {
    val errors by remember {
        mutableStateOf(
            validateLineData(
                data = data,
                style = style
            )
        )
    }

    if (errors.isEmpty()) {
        var title by remember { mutableStateOf(data.title) }
        var labels by remember { mutableStateOf(listOf<String>()) }

        val lineColors by remember {
            mutableStateOf(
                if (data.hasSingleItem()) {
                    listOf(style.lineColor)
                } else if (style.lineColors.isEmpty()) {
                    generateColorShades(style.lineColor, data.items.size)
                } else {
                    style.lineColors
                }
            )
        }
        ChartView(chartViewsStyle = style.chartViewStyle) {
            Text(
                modifier = style.chartViewStyle.modifierTopTitle
                    .testTag(TestTags.CHART_TITLE),
                text = title,
                style = style.chartViewStyle.styleTitle
            )

            LineChart(
                data = data,
                style = style,
                colors = lineColors
            ) { selectedIndex ->
                title = data.getLabel(selectedIndex)

                if (data.hasCategories()) {
                    labels = when (selectedIndex) {
                        NO_SELECTION -> emptyList()
                        else -> data.items.map { it.item.labels[selectedIndex] }
                    }
                }
            }

            if (data.hasCategories()) {
                LegendItem(
                    chartViewsStyle = style.chartViewStyle,
                    legend = data.items.map { it.label },
                    colors = lineColors,
                    labels = labels
                )
            }
        }
    } else {
        ChartErrors(style.chartViewStyle, errors)
    }
}
