package com.mathroda.cache.dbo.favoritecoins

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCoinsDao {

    @Query("SELECT * FROM FavoriteCoin")
    fun getAllFavoriteCoins(): Flow<List<FavoriteCoinEntity>>

    @Query("SELECT * FROM FavoriteCoin " +
            "Where CoinId = :coinId " +
            "Limit 1" )
    suspend fun getFavoriteCoinById(coinId: String): FavoriteCoinEntity?

    @Upsert
    suspend fun upsertFavoriteCoin(coin: FavoriteCoinEntity)

    @Upsert
    suspend fun insertAllFavoriteCoins(coins: List<FavoriteCoinEntity>)

    @Query("DELETE FROM FavoriteCoin " +
            "WHERE CoinId = :coinId")
    suspend fun deleteFavoriteCoin(coinId: String)

    @Query("SELECT COUNT(*) FROM FavoriteCoin")
    fun getFavoriteCoinsCount(): Flow<Int>

    @Query("DELETE FROM FavoriteCoin")
    suspend fun removeAllFavoriteCoins()
}