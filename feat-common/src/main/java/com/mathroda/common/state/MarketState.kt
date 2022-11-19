package com.mathroda.common.state

import com.mathroda.domain.CoinById

data class MarketState(
    val isLoading: Boolean = false,
    val coin: CoinById? = null,
    val error: String = ""
)
