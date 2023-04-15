package com.mathroda.network.dto

import com.mathroda.domain.model.CoinById

data class CoinDetail(
    val availableSupply: Double?,
    val exp: List<String>?,
    val icon: String?,
    val id: String?,
    val marketCap: Double?,
    val name: String?,
    val price: Double?,
    val priceBtc: Double?,
    val priceChange1d: Double?,
    val priceChange1h: Double?,
    val priceChange1w: Double?,
    val rank: Int?,
    val symbol: String?,
    val totalSupply: Double?,
    val twitterUrl: String?,
    val volume: Double?,
    val websiteUrl: String?
)

fun CoinDetail.toCoinDetail(): CoinById {
    return CoinById(
        availableSupply = availableSupply ?: 0.0,
        icon = icon ?: "",
        id = id ?: "",
        marketCap = marketCap ?: 0.0,
        name = name ?: "",
        price = price ?: 0.0,
        priceChange1d = priceChange1d ?: 0.0,
        priceChange1h = priceChange1h ?: 0.0,
        priceChange1w = priceChange1w ?: 0.0,
        rank = rank ?: 0,
        symbol = symbol ?: "",
        totalSupply = totalSupply ?: 0.0,
        twitterUrl = twitterUrl ?: "",
        volume = volume ?: 0.0,
        websiteUrl = websiteUrl ?: "",
        priceBtc = priceBtc ?: 0.0
    )
}