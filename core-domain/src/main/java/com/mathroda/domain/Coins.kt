package com.mathroda.domain

import java.util.*


data class Coins(
    val uniqueId: String = UUID.randomUUID().toString(),
    val id: String,
    val icon: String,
    val marketCap: Double,
    val name: String,
    val price: Double,
    val priceChange1d: Double,
    val rank: Int,
    val symbol: String
)
