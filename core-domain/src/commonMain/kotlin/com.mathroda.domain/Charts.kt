package com.mathroda.domain

data class Charts(
    val chart: List<ChartsData> = emptyList()
)

data class ChartsData(
    val timeStamp: Long,
    val price: Double,
    val volume: Double,
    val marketCap: Double
)
