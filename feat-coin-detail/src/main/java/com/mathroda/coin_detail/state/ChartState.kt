package com.mathroda.coin_detail.state

import com.mathroda.coin_detail.Point

data class ChartState(
    val isLoading: Boolean = false,
    val chart: List<Point> = emptyList(),
    val error: String = ""
)
