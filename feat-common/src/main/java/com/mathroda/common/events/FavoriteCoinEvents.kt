package com.mathroda.common.events

sealed class FavoriteCoinEvents {
    data class DeleteCoin(val coin: com.mathroda.domain.CoinById) : FavoriteCoinEvents()
    data class AddCoin(val coin: com.mathroda.domain.CoinById) : FavoriteCoinEvents()
}