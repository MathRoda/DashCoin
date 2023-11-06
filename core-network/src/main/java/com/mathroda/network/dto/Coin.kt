package com.mathroda.network.dto

import com.mathroda.domain.model.Coins

data class Coin(
    val availableSupply: Double? = null,
    val contractAddress: String ? = null,
    val decimals: Long? = null,
    val explorers: List<String>? = null,
    val icon: String? = null,
    val id: String,
    val marketCap: Double? = null,
    val name: String? = null,
    val price: Double? = null,
    val priceBtc: Double? = null,
    val priceChange1d: Double? = null,
    val priceChange1h: Double? = null,
    val priceChange1w: Double? = null,
    val rank: Int? = null,
    val redditUrl: String? = null,
    val symbol: String? = null,
    val totalSupply: Double? = null,
    val twitterUrl: String? = null,
    val volume: Double? = null,
    val websiteUrl: String? = null
)

fun Coin.toCoins(): Coins {
    return Coins(
        id = id,
        icon = icon ?: "",
        marketCap = marketCap ?: 0.0,
        name = name ?: "",
        price = price ?: 0.0,
        priceChange1d = priceChange1d ?: 0.0,
        rank = rank ?: 1,
        symbol = symbol ?: ""
    )
}





