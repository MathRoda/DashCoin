package com.mathroda.dashcoin.data.databaes

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mathroda.dashcoin.domain.model.CoinById
import com.mathroda.dashcoin.util.Constants

@Database(
    entities = [CoinById::class],
    version = 1
)
abstract class DashCoinDatabase: RoomDatabase() {

    abstract val dashCoinDao: DashCoinDao

    companion object {
      const val DATABASE_NAME = Constants.DATABASE_NAME
    }
}