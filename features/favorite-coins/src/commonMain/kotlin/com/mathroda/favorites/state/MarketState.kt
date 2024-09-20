package com.mathroda.favorites.state

import com.mathroda.domain.CoinById

data class MarketState(
    private val loading: Boolean = false,
    val coin: CoinById? = null,
    val error: String = ""
) {
    val isLoading: Boolean
        get() = loading && coin != null
}
