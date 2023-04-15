package com.example.cache.di

import android.app.Application
import androidx.room.Room
import com.example.cache.DashCoinDatabase
import com.example.cache.dbo.favoritecoins.FavoriteCoinsDao
import com.example.cache.dbo.user.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDashCoinDatabase(
        context: Application
    ): DashCoinDatabase {
        return Room.databaseBuilder(
                context,
                DashCoinDatabase::class.java,
                DashCoinDatabase.DATABASE_NAME
        ).build()
    }


    @Provides
    @Singleton
    fun providesFavoriteCoinsDao(
        database: DashCoinDatabase
    ): FavoriteCoinsDao {
        return database.favoriteCoinsDao
    }

    @Provides
    @Singleton
    fun providesUserDao(
        database: DashCoinDatabase
    ): UserDao {
        return database.userDao
    }
}