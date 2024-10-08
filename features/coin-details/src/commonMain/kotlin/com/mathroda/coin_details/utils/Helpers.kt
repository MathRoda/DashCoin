package com.mathroda.coin_details.utils


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochSeconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}


internal fun DrawScope.yAxisDrawing(
    upperValue: Float, lowerValue: Float,
    textMeasure: TextMeasurer,
    spacing: Dp,
    yAxisStyle: TextStyle,
    yAxisRange: Int,
) {
    val dataRange = upperValue - lowerValue
    val dataStep = dataRange / yAxisRange

    (0..yAxisRange).forEach { i ->
        val yValue = lowerValue + dataStep * i

        val y = (size.height.toDp() - spacing - i * (size.height.toDp() - spacing) / yAxisRange)
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                textMeasurer = textMeasure,
                text = yValue.formatToThousandsMillionsBillions(),
                style = yAxisStyle,
                topLeft = Offset(0f, y.toPx())
            )
        }
    }
}

private fun Float.formatToThousandsMillionsBillions(): String {
    val absValue = abs(this)
    return when {
        absValue < 1000 -> this.format("")
        absValue < 1_000_000 -> (this / 1_000).format("K")
        absValue < 1_000_000_000 -> (this / 1_000_000).format("M")
        absValue < 1_000_000_000_000 -> (this / 1_000_000_000).format("B")
        else -> "Infinity"
    }
}

fun Float.format(status:String): String {
    val intValue = this.toInt()
    val floatValue = this - intValue
    val decimalPart = (floatValue * 10).toInt()
    return "$intValue.$decimalPart$status"
}