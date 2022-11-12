package com.mathroda.network.dto

data class ChartDto(
    val chart: List<List<Float>>,
)

fun ChartDto.toChart(): com.mathroda.domain.Charts {
    return com.mathroda.domain.Charts(
        chart
    )
}