package com.mathroda.dashcoin.data.databaes

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.domain.model.CoinById

@Database(
    entities = [CoinById::class],
    version = 1,
    exportSchema = false
)
abstract class DashCoinDatabase: RoomDatabase() {

    abstract val dashCoinDao: DashCoinDao

    companion object {
      const val DATABASE_NAME = Constants.DATABASE_NAME
    }
}