package com.mathroda.chart.common.internal.barstackedchart

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mathroda.chart.common.style.ChartViewStyle

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun LegendItem(
    chartViewsStyle: ChartViewStyle,
    legend: List<String>,
    colors: List<Color>,
    labels: List<String> = emptyList()
) {
    FlowRow(
        modifier = chartViewsStyle.modifierLegend.animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        legend.forEachIndexed { index, legend ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .background(colors[index])
                )

                val label = when (labels.isEmpty()) {
                    true -> legend
                    else -> "$legend - ${labels[index]}"
                }

                Text(
                    text = label,
                    color = colors[index],
                    modifier = Modifier.padding(
                        start = chartViewsStyle.innerPadding,
                        end = chartViewsStyle.innerPadding
                    )
                )
            }
        }
    }
}
