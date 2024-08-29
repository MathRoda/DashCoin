package com.mathroda.network.dto

import com.mathroda.domain.Charts
import com.mathroda.domain.ChartsData
import kotlinx.serialization.Serializable

@Serializable
data class ChartDto(
    val chart: List<List<Double>>?,
)

fun ChartDto.toChart(): Charts {
    return Charts(
        chart = if (chart.isNullOrEmpty()) {
            emptyList()
        } else {
            chart.map {
                ChartsData(
                    timeStamp = it[0].toLong(),
                    price = it[1],
                    volume = it[2],
                    marketCap = it[3],
                )
            }
        }
    )
}