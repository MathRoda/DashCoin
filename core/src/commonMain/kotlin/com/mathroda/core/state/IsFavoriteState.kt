package com.mathroda.core.state

sealed class IsFavoriteState {
    data object Favorite : IsFavoriteState()
    data object NotFavorite : IsFavoriteState()

    data class Messages(
        val favoriteMessage: String = "",
        val notFavoriteMessage: String = ""
    )
}
