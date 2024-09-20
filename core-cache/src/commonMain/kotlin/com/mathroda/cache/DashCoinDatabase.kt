package com.mathroda.cache

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.mathroda.cache.converter.DateTypeConverter
import com.mathroda.cache.dbo.favoritecoins.FavoriteCoinEntity
import com.mathroda.cache.dbo.favoritecoins.FavoriteCoinsDao
import com.mathroda.cache.dbo.user.UserDao
import com.mathroda.cache.dbo.user.UserEntity
import com.mathroda.core.util.Constants

@Database(
    entities = [
        FavoriteCoinEntity::class,
        UserEntity::class
       ],
    version = 1,
    exportSchema = false
)
@ConstructedBy(DashCoinDatabaseConstructor::class)
@TypeConverters(DateTypeConverter::class)
abstract class DashCoinDatabase: RoomDatabase(){
    abstract val favoriteCoinsDao: FavoriteCoinsDao
    abstract val userDao: UserDao


    companion object {
        const val DATABASE_NAME = Constants.DB_NAME
    }
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object DashCoinDatabaseConstructor : RoomDatabaseConstructor<DashCoinDatabase>


