package com.mathroda.common.events

import com.example.shared.FavoriteCoin

sealed class FavoriteCoinEvents {
    data class DeleteCoin(val coin: com.example.shared.FavoriteCoin) : FavoriteCoinEvents()
    data class AddCoin(val coin: com.example.shared.FavoriteCoin) : FavoriteCoinEvents()
}