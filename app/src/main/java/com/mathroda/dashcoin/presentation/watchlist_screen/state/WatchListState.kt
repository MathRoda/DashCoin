package com.mathroda.dashcoin.presentation.watchlist_screen.state

import com.mathroda.dashcoin.domain.model.CoinById

data class WatchListState(
    val coin: List<CoinById> = emptyList()
)
