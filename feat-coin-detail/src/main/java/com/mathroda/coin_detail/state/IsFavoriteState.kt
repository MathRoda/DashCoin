package com.mathroda.coin_detail.state

sealed class IsFavoriteState {
    object Favorite : IsFavoriteState()
    object NotFavorite : IsFavoriteState()

    data class Messages(
        val favoriteMessage: String = "",
        val notFavoriteMessage: String = ""
    )
}
