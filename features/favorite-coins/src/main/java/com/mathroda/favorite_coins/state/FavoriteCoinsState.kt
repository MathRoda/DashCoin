package com.mathroda.favorite_coins.state

import com.mathroda.domain.model.FavoriteCoin

data class FavoriteCoinsState(
    val isLoading: Boolean = false,
    val coin: List<FavoriteCoin> = emptyList(),
    val isEmpty: Boolean = false,
    val error: String = "",
)
