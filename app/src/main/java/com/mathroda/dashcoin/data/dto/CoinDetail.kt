package com.mathroda.dashcoin.data.dto

import com.mathroda.dashcoin.domain.model.CoinById

data class CoinDetail(
    val availableSupply: Double,
    val exp: List<String>,
    val icon: String,
    val id: String,
    val marketCap: Double,
    val name: String,
    val price: Double,
    val priceBtc: Double,
    val priceChange1d: Double,
    val priceChange1h: Double,
    val priceChange1w: Double,
    val rank: Int,
    val symbol: String,
    val totalSupply: Double,
    val twitterUrl: String,
    val volume: Double,
    val websiteUrl: String
)

fun CoinDetail.toCoinDetail(): CoinById {
    return CoinById(
        availableSupply = availableSupply,
        icon = icon,
        id = id,
        marketCap = marketCap,
        name = name,
        price = price,
        priceChange1d = priceChange1d,
        priceChange1h = priceChange1h,
        priceChange1w = priceChange1w,
        rank = rank,
        symbol = symbol,
        totalSupply = totalSupply,
        twitterUrl = twitterUrl,
        volume = volume,
        websiteUrl = websiteUrl,
        priceBtc = priceBtc
    )
}