package io.github.dautovicharis.charts.style

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.dautovicharis.charts.internal.DONUT_MAX_PERCENTAGE
import io.github.dautovicharis.charts.internal.DONUT_MIN_PERCENTAGE

/**
 * A class that defines the style for a Pie Chart.
 *
 * @property modifier The modifier to be applied to the chart.
 * @property chartViewStyle The style to be applied to the chart view.
 * @property donutPercentage The percentage of the chart that should be a donut hole. Must be between DONUT_MIN_PERCENTAGE and DONUT_MAX_PERCENTAGE.
 * @property pieColors The colors to be used for the slices in the pie chart.
 * @property pieColor The color to be used for the pie chart if pieColors is empty.
 * @property borderColor The color of the border around the pie chart.
 * @property borderWidth The width of the border around the pie chart.
 */
@Immutable
class PieChartStyle internal constructor(
    internal val modifier: Modifier,
    internal val chartViewStyle: ChartViewStyle,
    val donutPercentage: Float,
    var pieColors: List<Color>,
    val pieColor: Color,
    val borderColor: Color,
    val borderWidth: Float
) : Style {
    /**
     * Returns a list of the properties of the PieChartStyle.
     */
    override fun getProperties(): List<Pair<String, Any>> {
        return listOf(
            PieChartStyle::donutPercentage.name to donutPercentage,
            PieChartStyle::pieColors.name to pieColors,
            PieChartStyle::pieColor.name to pieColor,
            PieChartStyle::borderColor.name to borderColor,
            PieChartStyle::borderWidth.name to borderWidth
        )
    }
}

/**
 * An object that provides default styles for a Pie Chart.
 */
object PieChartDefaults {
    /**
     * Returns a PieChartStyle with the provided parameters or their default values.
     *
     * @param pieColor The color to be used for the pie chart if pieColors is empty. Defaults to the primary color of the MaterialTheme.
     * @param pieColors The colors to be used for the slices in the pie chart. Defaults to an empty list.
     * @param borderColor The color of the border around the pie chart. Defaults to the surface color of the MaterialTheme.
     * @param innerPadding The inner padding of the pie chart. Defaults to 15.dp.
     * @param donutPercentage The percentage of the chart that should be a donut hole. Defaults to 0f.
     * @param borderWidth The width of the border around the pie chart. Defaults to 3f.
     * @param chartViewStyle The style to be applied to the chart view. Defaults to the default style of ChartViewDefaults.
     */
    @Composable
    fun style(
        pieColor: Color = MaterialTheme.colorScheme.primary,
        pieColors: List<Color> = emptyList(),
        borderColor: Color = MaterialTheme.colorScheme.surface,
        innerPadding: Dp = 15.dp,
        donutPercentage: Float = 0f,
        borderWidth: Float = 3f,
        chartViewStyle: ChartViewStyle = ChartViewDefaults.style()
    ): PieChartStyle {
        return PieChartStyle(
            modifier = Modifier
                .wrapContentSize()
                .padding(innerPadding)
                .aspectRatio(1f),
            donutPercentage = donutPercentage.coerceIn(
                DONUT_MIN_PERCENTAGE,
                DONUT_MAX_PERCENTAGE
            ),
            pieColors = pieColors,
            pieColor = pieColor,
            borderColor = borderColor,
            borderWidth = borderWidth,
            chartViewStyle = chartViewStyle
        )
    }
}
