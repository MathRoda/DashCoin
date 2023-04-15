package com.mathroda.coins_screen.state

import com.mathroda.domain.model.Coins

data class CoinsState(
    val isLoading: Boolean = false,
    val coins: List<Coins> = emptyList(),
    val error: String = ""
    )
