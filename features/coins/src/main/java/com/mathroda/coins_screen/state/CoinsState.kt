package com.mathroda.coins_screen.state

import com.mathroda.domain.model.Coins
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CoinsState(
    val isLoading: Boolean = false,
    val coins: ImmutableList<Coins> = persistentListOf(),
    val error: String = ""
    )
