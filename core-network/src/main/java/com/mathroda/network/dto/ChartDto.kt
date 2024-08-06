package com.mathroda.network.dto

import com.mathroda.domain.model.Charts
import kotlinx.serialization.Serializable

@Serializable
data class ChartDto(
    val chart: List<List<Float>>,
)

fun ChartDto.toChart(): Charts {
    return Charts(
        chart
    )
}