package com.mathroda.domain

import com.mathroda.core.util.getCurrentDate
import com.mathroda.core.util.getCurrentDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteCoin(
    val coinId: String = "",
    val name: String = "",
    val symbol: String = "",
    val icon: String = "",
    val price: Double = 0.0,
    val rank: Int = 0,
    val priceChanged1d: Double = 0.0,
    val priceChanged1h: Double = 0.0,
    val priceChanged1w: Double = 0.0,
    val lastUpdated: LocalDateTime = getCurrentDateTime()
)
