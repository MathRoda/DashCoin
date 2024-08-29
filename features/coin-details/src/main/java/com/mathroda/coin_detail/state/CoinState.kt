package com.mathroda.coin_detail.state

import com.example.shared.CoinById

data class CoinState(
    val isLoading: Boolean = false,
    val coin: com.example.shared.CoinById? = null,
    val error: String = ""
)
