package com.mathroda.cache.di

import android.app.Application
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mathroda.cache.DashCoinDatabase
import com.mathroda.cache.DashCoinDatabase.Companion.DATABASE_NAME
import com.mathroda.cache.datastore.DashCoinDataStore
import com.mathroda.cache.datastore.dataStoreFileName
import com.mathroda.cache.datastore.initDataStore
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual fun platformModule() = module {
    single { Factory(androidApplication()) }
    single<DashCoinDatabase> {
        val factory = get<Factory>()
        factory.createRoomDatabase()
    }
    single<DashCoinDataStore> {
        val factory = get<Factory>()
        factory.createDataStore()
    }
}

actual class Factory(private val context: Application) {
    actual fun createRoomDatabase(): DashCoinDatabase {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        return Room.databaseBuilder<DashCoinDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    actual fun createDataStore(): DashCoinDataStore {
        val dataStore = initDataStore {
            context.filesDir.resolve(dataStoreFileName).absolutePath
        }
        return DashCoinDataStore(dataStore)
    }


}
