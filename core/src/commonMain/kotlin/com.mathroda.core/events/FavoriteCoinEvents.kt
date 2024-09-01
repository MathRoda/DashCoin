package com.mathroda.core.events

import com.mathroda.domain.FavoriteCoin

sealed class FavoriteCoinEvents {
    data class DeleteCoin(val coin: FavoriteCoin) : FavoriteCoinEvents()
    data class AddCoin(val coin: FavoriteCoin) : FavoriteCoinEvents()
}