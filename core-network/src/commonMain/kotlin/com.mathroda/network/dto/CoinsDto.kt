package com.mathroda.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinsDto(
    val result: List<Coin>
)