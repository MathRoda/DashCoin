package com.mathroda.coins.state

import com.mathroda.domain.Coins
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CoinsState(
    val isLoading: Boolean = false,
    val coins: ImmutableList<Coins> = persistentListOf(),
    val error: String = ""
    )
