package com.mathroda.dashcoin.presentation.coins_screen.state

import com.mathroda.dashcoin.domain.model.Coins

data class CoinsState(
    val isLoading: Boolean = false,
    val coins: List<Coins> = emptyList(),
    val error: String = ""
)
