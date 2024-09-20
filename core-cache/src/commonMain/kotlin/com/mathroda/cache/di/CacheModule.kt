package com.mathroda.cache.di

import com.mathroda.cache.DashCoinDatabase
import com.mathroda.cache.datastore.DashCoinDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

expect class Factory {
    fun createRoomDatabase(): DashCoinDatabase
    fun createDataStore(): DashCoinDataStore
}

val cacheModule = module {
    includes(platformModule())
    single { get<DashCoinDatabase>().favoriteCoinsDao }
    single { get<DashCoinDatabase>().userDao }
}