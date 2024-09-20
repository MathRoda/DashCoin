package com.mathroda.cache.dbo.favoritecoins

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mathroda.cache.converter.DateTypeConverter
import com.mathroda.core.util.getCurrentDateTime
import com.mathroda.domain.FavoriteCoin
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "FavoriteCoin"
)
data class FavoriteCoinEntity(
    @PrimaryKey
    @ColumnInfo(name = "CoinId") val coinId: String,
    @ColumnInfo(name = "Name") val name: String?,
    @ColumnInfo(name = "Symbol") val symbol: String?,
    @ColumnInfo(name = "Icon") val icon: String?,
    @ColumnInfo(name = "Price") val price: Double?,
    @ColumnInfo(name = "Rank") val rank: Int,
    @ColumnInfo(name = "PriceChanged1d") val priceChanged1d: Double?,
    @ColumnInfo(name = "PriceChanged1h") val priceChanged1h: Double?,
    @ColumnInfo(name = "PriceChanged1w") val priceChanged1w: Double?,
    @TypeConverters(DateTypeConverter::class)
    @ColumnInfo(name = "LastUpdated") val lastUpdated: LocalDateTime?

)

fun FavoriteCoinEntity.toDomain(): FavoriteCoin {
    return FavoriteCoin(
        coinId = coinId,
        name = name ?: "",
        symbol = symbol ?: "",
        icon = icon ?: "",
        price = price ?: 0.0,
        rank = rank,
        priceChanged1d = priceChanged1d ?: 0.0,
        priceChanged1h = priceChanged1h ?: 0.0,
        priceChanged1w = priceChanged1w ?: 0.0,
        lastUpdated = lastUpdated ?: getCurrentDateTime()
    )
}

fun FavoriteCoin.toEntity(): FavoriteCoinEntity {
    return FavoriteCoinEntity(
        coinId = coinId,
        name = name,
        symbol = symbol,
        icon = icon,
        price = price,
        rank = rank,
        priceChanged1d = priceChanged1d,
        priceChanged1h = priceChanged1h,
        priceChanged1w = priceChanged1w,
        lastUpdated = lastUpdated
    )
}

