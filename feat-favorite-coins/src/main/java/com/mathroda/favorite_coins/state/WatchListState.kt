package com.mathroda.favorite_coins.state

import com.mathroda.domain.CoinById

data class WatchListState(
    val isLoading: Boolean = false,
    val coin: List<CoinById> = emptyList(),
    val isEmpty: Boolean = false,
    val error: String = ""
)
