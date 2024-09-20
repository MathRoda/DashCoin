package com.mathroda.coin_details.state

import com.mathroda.coin_details.Point

data class ChartState(
    val isLoading: Boolean = false,
    val chart: List<Point> = emptyList(),
    val error: String = ""
)
