package com.mathroda.coin_detail.state

import com.mathroda.domain.Charts

data class ChartState(
    val isLoading: Boolean = false,
    val chart: Charts? = null,
    val error: String = ""
)
