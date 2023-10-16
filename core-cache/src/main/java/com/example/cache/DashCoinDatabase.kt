package com.example.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cache.converter.DateTypeConverter
import com.example.cache.dbo.favoritecoins.FavoriteCoinEntity
import com.example.cache.dbo.favoritecoins.FavoriteCoinsDao
import com.example.cache.dbo.user.UserDao
import com.example.cache.dbo.user.UserEntity
import com.mathroda.core.util.Constants

@Database(
    entities = [
        FavoriteCoinEntity::class,
        UserEntity::class
       ],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateTypeConverter::class)
abstract class DashCoinDatabase: RoomDatabase(){
    abstract val favoriteCoinsDao: FavoriteCoinsDao
    abstract val userDao: UserDao


    companion object {
        const val DATABASE_NAME = Constants.DB_NAME
    }
}