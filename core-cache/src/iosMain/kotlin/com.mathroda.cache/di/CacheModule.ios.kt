@file:OptIn(ExperimentalForeignApi::class)

package com.mathroda.cache.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mathroda.cache.DashCoinDatabase
import com.mathroda.cache.DashCoinDatabase.Companion.DATABASE_NAME
import com.mathroda.cache.datastore.DashCoinDataStore
import com.mathroda.cache.datastore.dataStoreFileName
import com.mathroda.cache.datastore.initDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask


actual fun platformModule() = module {
    single<DashCoinDatabase> { Factory().createRoomDatabase() }
    single<DashCoinDataStore> { Factory().createDataStore() }
}

actual class Factory {
    actual fun createRoomDatabase(): DashCoinDatabase {
        val dbFile = "${fileDirectory()}/$DATABASE_NAME"
        return Room.databaseBuilder<DashCoinDatabase>(
            name = dbFile,
        )
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    actual fun createDataStore(): DashCoinDataStore {
        val dataStore = initDataStore {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            requireNotNull(documentDirectory).path + "/$dataStoreFileName"
        }

        return DashCoinDataStore(dataStore)
    }

}

@OptIn(ExperimentalForeignApi::class)
private fun fileDirectory(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory).path!!
}