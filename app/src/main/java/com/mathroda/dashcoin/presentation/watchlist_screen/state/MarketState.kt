package com.mathroda.dashcoin.presentation.watchlist_screen.state

import com.mathroda.dashcoin.domain.model.CoinById

data class MarketState(
    val isLoading: Boolean = false,
    val coin: CoinById?= null ,
    val error: String = ""
)
