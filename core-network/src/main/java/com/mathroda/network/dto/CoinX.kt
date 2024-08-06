package com.mathroda.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinX(
    val coinIdKeyWords: String,
    val coinKeyWords: String,
    val coinNameKeyWords: String,
    val coinPercent: Double,
    val coinTitleKeyWords: String
)