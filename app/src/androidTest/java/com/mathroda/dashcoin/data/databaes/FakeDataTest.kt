package com.mathroda.dashcoin.data.databaes

import com.mathroda.dashcoin.domain.model.CoinById

object FakeDataTest {

    val coinById = CoinById(
        availableSupply = 1.0,
        icon = "icon",
        id = "id",
        marketCap = 1.0,
        name = "name",
        price = 1.0,
        priceChange1d = 1.0,
        priceChange1h = 1.0,
        priceChange1w = 1.0,
        rank = 1,
        symbol = "symbol",
        totalSupply = 1.0,
        twitterUrl = "twitterUrl",
        volume = 1.0,
        websiteUrl = "websiteUrl"
    )
}