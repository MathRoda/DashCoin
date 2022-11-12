package com.mathroda.coin_detail.state

data class CoinState(
    val isLoading: Boolean = false,
    val coin: com.mathroda.domain.CoinById? = null,
    val error: String = ""
)
