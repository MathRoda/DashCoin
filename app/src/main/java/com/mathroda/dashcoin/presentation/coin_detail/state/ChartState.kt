package com.mathroda.dashcoin.presentation.coin_detail.state

import com.mathroda.dashcoin.domain.model.Charts

data class ChartState(
    val isLoading: Boolean = false,
    val chart: Charts?= null ,
    val error: String = ""
)
