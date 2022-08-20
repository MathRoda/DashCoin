package com.mathroda.dashcoin.presentation.watchlist_screen.state

import com.mathroda.dashcoin.domain.model.CoinById

data class WatchListState(
    val isLoading: Boolean = false,
    val coin: List<CoinById> = emptyList(),
    val error: String? = null
)
