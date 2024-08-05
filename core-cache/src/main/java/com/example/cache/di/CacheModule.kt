package com.example.cache.di

import androidx.room.Room
import com.example.cache.DashCoinDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val cacheModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            DashCoinDatabase::class.java,
            DashCoinDatabase.DATABASE_NAME
        ).build()
    }

    single { get<DashCoinDatabase>().favoriteCoinsDao }
    single { get<DashCoinDatabase>().userDao }
}