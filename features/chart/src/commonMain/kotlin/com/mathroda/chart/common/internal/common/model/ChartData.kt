package com.mathroda.chart.common.internal.common.model

internal class ChartData(val data: List<Pair<String, Double>>) {
    val labels: List<String> get() = data.map { it.first }
    val points: List<Double> get() = data.map { it.second }
}


internal fun List<Float>.toChartData(
    prefix: String = "",
    postfix: String = ""
): ChartData = ChartData(this.map { "${prefix}${it}${postfix}" to it.toDouble() })
