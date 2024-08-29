package com.mathroda.network.dto

import com.mathroda.domain.CoinById
import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailDto(
    val availableSupply: Double? = null,
    val contractAddress: String ? = null,
    val decimals: Long? = null,
    val explorers: List<String>? = null,
    val icon: String? = null,
    val id: String? = null,
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

fun CoinDetailDto.toCoinDetail(): CoinById {
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