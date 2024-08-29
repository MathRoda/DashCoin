package com.mathroda.coins_screen.state

import com.example.shared.Coins
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CoinsState(
    val isLoading: Boolean = false,
    val coins: ImmutableList<com.example.shared.Coins> = persistentListOf(),
    val error: String = ""
    )
