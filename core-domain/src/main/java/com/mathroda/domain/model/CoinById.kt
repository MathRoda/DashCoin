package com.mathroda.domain.model

data class CoinById(
    val availableSupply: Double = 0.0,
    val icon: String = "",
    val id: String = "",
    val marketCap: Double = 0.0,
    val name: String = "",
    val price: Double = 0.0,
    val priceChange1d: Double = 0.0,
    val priceChange1h: Double = 0.0,
    val priceChange1w: Double = 0.0,
    val rank: Int = 0,
    val symbol: String = "",
    val totalSupply: Double = 0.0,
    val twitterUrl: String = "",
    val volume: Double = 0.0,
    val websiteUrl: String = "",
    val priceBtc: Double = 0.0,
)

fun CoinById.toFavoriteCoin(): FavoriteCoin {
    return FavoriteCoin(
        coinId = id,
        name = name,
        symbol = symbol,
        price = price,
        icon = icon,
        rank = rank,
        priceChanged1w = priceChange1w,
        priceChanged1h = priceChange1h,
        priceChanged1d = priceChange1d
    )
}
