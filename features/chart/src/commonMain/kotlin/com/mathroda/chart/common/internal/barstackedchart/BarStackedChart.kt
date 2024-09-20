package com.mathroda.chart.common.internal.barstackedchart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.util.lerp
import com.mathroda.chart.common.internal.ANIMATION_TARGET
import com.mathroda.chart.common.internal.AnimationSpec
import com.mathroda.chart.common.internal.DEFAULT_SCALE
import com.mathroda.chart.common.internal.MAX_SCALE
import com.mathroda.chart.common.internal.NO_SELECTION
import com.mathroda.chart.common.internal.TestTags
import com.mathroda.chart.common.internal.barchart.getSelectedIndex
import com.mathroda.chart.common.internal.common.model.MultiChartData
import com.mathroda.chart.common.style.StackedBarChartStyle

@Composable
internal fun StackedBarChart(
    data: MultiChartData,
    style: StackedBarChartStyle,
    colors: List<Color>,
    onValueChanged: (Int) -> Unit = {}
) {
    val progress = remember {
        data.items.map { Animatable(0f) }
    }
    var selectedIndex by remember { mutableIntStateOf(-1) }

    progress.forEachIndexed { index, _ ->
        LaunchedEffect(index) {
            progress[index].animateTo(
                targetValue = ANIMATION_TARGET,
                animationSpec = AnimationSpec.stackedBar(index)
            )
        }
    }

    Canvas(
        modifier = style.modifier
            .testTag(TestTags.STACKED_BAR_CHART)
            .pointerInput(Unit) {
            detectDragGestures(
                onDrag = { change, _ ->
                    selectedIndex =
                        getSelectedIndex(
                            position = change.position,
                            dataSize = data.items.count(),
                            canvasSize = size
                        )
                    onValueChanged(selectedIndex)
                },
                onDragEnd = {
                    selectedIndex = NO_SELECTION
                    onValueChanged(NO_SELECTION)
                }
            )
        }, onDraw = {
            drawBars(
                style = style,
                size = size,
                data = data,
                progress = progress,
                selectedIndex = selectedIndex,
                colors = colors
            )
        }
    )
}

private fun DrawScope.drawBars(
    style: StackedBarChartStyle,
    size: Size,
    data: MultiChartData,
    progress: List<Animatable<Float, AnimationVector1D>>,
    selectedIndex: Int,
    colors: List<Color>
) {
    val totalMaxValue = data.items.maxOf { it.item.points.sum() }
    val spacing = style.space.toPx()
    val barWidth = (size.width - spacing * (data.items.size - 1)) / data.items.size

    data.items.forEachIndexed { index, item ->
        var topOffset = size.height
        val selectedBarScale = if (index == selectedIndex) MAX_SCALE else DEFAULT_SCALE
        item.item.points.forEachIndexed { dataIndex, value ->
            val height = lerp(
                0f,
                (value.toFloat() * selectedBarScale / totalMaxValue.toFloat()) * size.height,
                progress[index].value
            )
            topOffset -= height

            drawRect(
                color = colors[dataIndex],
                topLeft = Offset(x = index * (barWidth + spacing), y = topOffset),
                size = Size(
                    width = barWidth * selectedBarScale,
                    height = height
                )
            )
        }
    }
}
