package com.mathroda.cache.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mathroda.cache.DashCoinDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val cacheModule = module {
    platformModule()
    single { getRoomDatabase(get()) }
    single { get<DashCoinDatabase>().favoriteCoinsDao }
    single { get<DashCoinDatabase>().userDao }
}

private fun getRoomDatabase(
    builder: RoomDatabase.Builder<DashCoinDatabase>
): DashCoinDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}