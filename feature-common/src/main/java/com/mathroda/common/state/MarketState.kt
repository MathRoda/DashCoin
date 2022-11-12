package com.mathroda.common.state

data class MarketState(
    val isLoading: Boolean = false,
    val coin: com.mathroda.domain.CoinById? = null,
    val error: String = ""
)
