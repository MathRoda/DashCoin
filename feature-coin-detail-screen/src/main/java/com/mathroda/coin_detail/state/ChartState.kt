package com.mathroda.coin_detail.state

data class ChartState(
    val isLoading: Boolean = false,
    val chart: com.mathroda.domain.Charts? = null,
    val error: String = ""
)
