package com.mathroda.favorite_coins.state

data class WatchListState(
    val isLoading: Boolean = false,
    val coin: List<com.mathroda.domain.CoinById> = emptyList(),
    val error: String? = null
)