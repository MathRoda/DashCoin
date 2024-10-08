package com.mathroda.coin_details.state

import com.mathroda.domain.CoinById

data class CoinState(
    val isLoading: Boolean = false,
    val coin: CoinById? = null,
    val error: String = ""
)
