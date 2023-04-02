package com.mathroda.coin_detail.state

import com.mathroda.domain.CoinById

data class CoinState(
    val isLoading: Boolean = false,
    val coin: CoinById? = null,
    val error: String = ""
)
