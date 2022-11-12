package com.mathroda.domain


data class Coins(
    val id: String,
    val icon: String,
    val marketCap: Double,
    val name: String,
    val price: Double,
    val priceChange1d: Double,
    val rank: Int,
    val symbol: String
)
