package com.mathroda.network.dto

import com.mathroda.domain.model.Charts

data class ChartDto(
    val chart: List<List<Float>>,
)

fun ChartDto.toChart(): Charts {
    return Charts(
        chart
    )
}