package com.mathroda.cache.mapper


import com.mathroda.cache.dbo.favoritecoins.FavoriteCoinEntity
import com.mathroda.core.util.getCurrentDate
import com.mathroda.core.util.getCurrentDateTime
import com.mathroda.domain.FavoriteCoin

object DatabaseMapper: EntityMapper<List<FavoriteCoin>, List<FavoriteCoinEntity>> {

    override fun toDomain(entity: List<FavoriteCoinEntity>): List<FavoriteCoin> {
        return entity.map { data ->
            FavoriteCoin(
                coinId = data.coinId,
                name = data.name ?: "",
                symbol = data.symbol ?: "",
                icon = data.icon ?: "",
                price = data.price ?: 0.0,
                rank = data.rank,
                priceChanged1d = data.priceChanged1d ?: 0.0,
                priceChanged1h = data.priceChanged1h ?: 0.0,
                priceChanged1w = data.priceChanged1w ?: 0.0,
                lastUpdated = data.lastUpdated ?: getCurrentDateTime()
            )
        }
    }

    override fun toEntity(domain: List<FavoriteCoin>): List<FavoriteCoinEntity> {
        return domain.map { data ->
            FavoriteCoinEntity(
                coinId = data.coinId,
                name = data.name,
                symbol = data.symbol,
                icon = data.icon,
                price = data.price,
                rank = data.rank,
                priceChanged1d = data.priceChanged1d,
                priceChanged1h = data.priceChanged1h,
                priceChanged1w = data.priceChanged1w,
                lastUpdated = data.lastUpdated
            )
        }
    }
}

fun List<FavoriteCoinEntity>.toDomain() = DatabaseMapper.toDomain(this)

fun List<FavoriteCoin>.toEntity() = DatabaseMapper.toEntity(this)