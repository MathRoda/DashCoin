package com.mathroda.common.events

import com.mathroda.domain.CoinById

sealed class FavoriteCoinEvents {
    data class DeleteCoin(val coin: CoinById) : FavoriteCoinEvents()
    data class AddCoin(val coin: CoinById) : FavoriteCoinEvents()
}