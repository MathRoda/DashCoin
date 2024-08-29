package com.mathroda.common.state

import com.example.shared.CoinById

data class MarketState(
    private val loading: Boolean = false,
    val coin: com.example.shared.CoinById? = null,
    val error: String = ""
) {
    val isLoading: Boolean
        get() = loading && coin != null
}
