package com.mathroda.dashcoin.data.remot.dto

import com.mathroda.dashcoin.domain.model.Charts

data class ChartDto(
    val chart: List<List<Double>>
)

fun ChartDto.toChart(): Charts{
    return Charts(
        chart
    )
}