package com.mathroda.favorite_coins.state

import com.example.shared.FavoriteCoin

data class FavoriteCoinsState(
    val isLoading: Boolean = false,
    val coin: List<com.example.shared.FavoriteCoin> = emptyList(),
    val isEmpty: Boolean = false,
    val error: String = "",
)
