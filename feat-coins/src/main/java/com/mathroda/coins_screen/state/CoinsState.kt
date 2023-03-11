package com.mathroda.coins_screen.state

import com.mathroda.domain.Coins

data class CoinsState(
    val isLoading: Boolean = false,
    val coins: List<Coins> = emptyList(),
    val error: String = ""
    )
