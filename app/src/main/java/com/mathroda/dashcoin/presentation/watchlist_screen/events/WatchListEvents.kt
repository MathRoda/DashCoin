package com.mathroda.dashcoin.presentation.watchlist_screen.events

import com.mathroda.dashcoin.domain.model.CoinById

sealed class WatchListEvents {
    data class DeleteCoin(val coin: CoinById): WatchListEvents()
    data class AddCoin(val coin: CoinById): WatchListEvents()
    object RestoreDeletedCoin: WatchListEvents()
}