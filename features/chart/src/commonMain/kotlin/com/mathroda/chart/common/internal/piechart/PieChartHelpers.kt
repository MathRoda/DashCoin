package com.mathroda.chart.common.internal.piechart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import com.mathroda.chart.common.internal.NO_SELECTION
import com.mathroda.chart.common.internal.common.model.ChartData
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Checks whether the given [pointX] and [pointY] is inside a circle with the specified [size].
 *
 * @param [pointX] and [pointY] The input representing the position of a point.
 * @param size The size of the circle as [IntSize].
 * @return `true` if the point is inside the circle, `false` otherwise.
 */
internal fun isPointInCircle(pointX: Float, pointY: Float, size: IntSize): Boolean {
    // Calculate the center coordinates of the circle
    val centerX = size.center.x
    val centerY = size.center.y

    // Calculate the radius of the circle as half of the minimum dimension (width or height)
    val radius = min(size.width, size.height) / 2

    // Calculate the distance between the point and the center of the circle using the Pythagorean theorem
    val dx = pointX - centerX
    val dy = pointY - centerY
    val distance = sqrt(dx * dx + dy * dy)

    // The point is inside the circle if the distance is less than or equal to the radius
    return distance <= radius
}

/**
 * Calculates the degree based on the position of the given [point] relative to the center of a circle with [size].
 *
 * @param [pointX] and [pointY] The input representing the position of a point.
 * @param size The size of the circle as [IntSize].
 * @return The degree of the point in relation to the center of the circle.
 */
internal fun degree(pointX: Float, pointY: Float, size: IntSize): Double {
    // Calculate the differences in x and y coordinates between the point and the center of the circle
    val dx = pointX - size.center.x
    val dy = pointY - size.center.y

    // Calculate the acute angle in degrees
    val acuteDegree = atan(dy / dx) * (180 / PI)

    // Determine the quadrant in which the point lies
    val isInBottomRight = dx >= 0 && dy >= 0
    val isInBottomLeft = dx <= 0 && dy >= 0
    val isInTopLeft = dx <= 0 && dy <= 0
    val isInTopRight = dx >= 0 && dy <= 0

    // Adjust the degree based on the quadrant
    val degree = when {
        isInBottomRight -> acuteDegree
        isInBottomLeft -> 180.0 - abs(acuteDegree)
        isInTopLeft -> 180.0 + abs(acuteDegree)
        isInTopRight -> 360.0 - abs(acuteDegree)
        else -> 0.0
    }
    return degree
}

internal fun getSelectedIndex(
    pointX: Float,
    pointY: Float,
    size: IntSize,
    slices: List<PieSlice>
): Int {
    return when (isPointInCircle(pointX = pointX, pointY = pointY, size = size)) {
        true -> {
            val touchDegree = degree(pointX = pointX, pointY = pointY, size = size)
            slices
                .indexOfFirst { it.startDeg < touchDegree && it.endDeg > touchDegree }
                .takeIf { it != NO_SELECTION } ?: NO_SELECTION
        }

        else -> NO_SELECTION
    }
}

internal fun createPieSlices(data: ChartData): List<PieSlice> {
    return mutableListOf<PieSlice>().apply {
        var lastEndDeg = 0.0
        val maxValue = data.points.sum()
        for (slice in data.points) {
            val normalized = slice / maxValue
            val startDeg = lastEndDeg
            val endDeg = lastEndDeg + (normalized * 360)
            lastEndDeg = endDeg
            add(
                PieSlice(
                    startDeg = startDeg.toFloat(),
                    endDeg = endDeg.toFloat(),
                    value = slice,
                    sweepAngle = (endDeg - startDeg).toFloat(),
                    normalizedValue = normalized
                )
            )
        }
    }
}

/**
 * Calculates the coordinates for the middle of a slice in a pie chart.
 *
 * @param index The index of the slice in the list of slices.
 * @param size The size of the pie chart as [IntSize].
 * @param slices The list of slices in the pie chart.
 * @return The [Offset] representing the coordinates of the middle of the slice.
 */
internal fun getCoordinatesForSlice(
    index: Int,
    size: IntSize,
    slices: List<PieSlice>
): Offset {
    val slice = slices[index]
    val startAngle = slice.startDeg
    val sweepAngle = slice.sweepAngle
    val radius = size.width / 2

    // Calculate midpoint angle of the slice
    val midAngle = startAngle + (sweepAngle / 2f)

    // Convert midpoint angle from degrees to radians
    val radian = midAngle * (PI / 180)

    // Calculate the distance from the center to the middle of the slice
    val middleRadius = radius / 2f

    // Calculate x and y coordinates of the middle of the slice
    val x = radius + middleRadius * cos(radian).toFloat()
    val y = radius + middleRadius * sin(radian).toFloat()

    return Offset(x, y)
}

