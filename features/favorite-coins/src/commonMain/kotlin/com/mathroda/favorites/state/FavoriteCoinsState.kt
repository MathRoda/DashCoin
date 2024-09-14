package com.mathroda.favorites.state

import com.mathroda.domain.FavoriteCoin

data class FavoriteCoinsState(
    val isLoading: Boolean = false,
    val coin: List<FavoriteCoin> = emptyList(),
    val isEmpty: Boolean = false,
    val error: String = "",
)
