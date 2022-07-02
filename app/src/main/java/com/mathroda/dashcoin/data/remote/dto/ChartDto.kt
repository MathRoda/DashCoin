package com.mathroda.dashcoin.data.remote.dto

import com.mathroda.dashcoin.domain.model.Charts

data class ChartDto(
    val chart: List<List<Float>>,
)

fun ChartDto.toChart(): Charts{
    return Charts(
        chart
    )
}