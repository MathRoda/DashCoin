package com.mathroda.dashcoin.presentation.watchlist_screen.state

data class MarketState(
    val isLoading: Boolean = false,
    val coin: com.mathroda.domain.CoinById?= null,
    val error: String = ""
)
