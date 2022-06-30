package com.mathroda.dashcoin.data.databaes

import androidx.room.*
import com.mathroda.dashcoin.domain.model.CoinById
import kotlinx.coroutines.flow.Flow


@Dao
interface DashCoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coinsById: CoinById)

    @Delete
    suspend fun deleteCoin(coinById: CoinById)

    @Query("SELECT * FROM coinbyid")
    fun getAllCoins(): Flow<List<CoinById>>
}