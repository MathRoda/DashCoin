package com.mathroda.common.events

import com.mathroda.domain.model.FavoriteCoin

sealed class FavoriteCoinEvents {
    data class DeleteCoin(val coin: FavoriteCoin) : FavoriteCoinEvents()
    data class AddCoin(val coin: FavoriteCoin) : FavoriteCoinEvents()
}