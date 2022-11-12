package com.mathroda.coins_screen.state

data class CoinsState(
    val isLoading: Boolean = false,
    val coins: List<com.mathroda.domain.Coins> = emptyList(),
    val error: String = ""
)
