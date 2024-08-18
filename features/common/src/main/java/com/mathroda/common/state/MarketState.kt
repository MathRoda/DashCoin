package com.mathroda.common.state

import com.mathroda.domain.model.CoinById

data class MarketState(
    private val loading: Boolean = false,
    val coin: CoinById? = null,
    val error: String = ""
) {
    val isLoading: Boolean
        get() = loading && coin != null
}
