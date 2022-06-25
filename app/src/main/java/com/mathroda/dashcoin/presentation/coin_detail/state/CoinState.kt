package com.mathroda.dashcoin.presentation.coin_detail.state

import com.mathroda.dashcoin.domain.model.CoinById

data class CoinState(
    val isLoading: Boolean = false,
    val coin: CoinById?= null ,
    val error: String = ""
)
