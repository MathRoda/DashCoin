package com.mathroda.domain

import com.mathroda.core.util.generateUUID


data class Coins(
    val uniqueId: String = generateUUID(),
    val id: String,
    val icon: String,
    val marketCap: Double,
    val name: String,
    val price: Double,
    val priceChange1d: Double,
    val rank: Int,
    val symbol: String
)
