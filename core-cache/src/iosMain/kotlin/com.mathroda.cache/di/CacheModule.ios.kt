@file:OptIn(ExperimentalForeignApi::class)

package com.mathroda.cache.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.mathroda.cache.DashCoinDatabase
import com.mathroda.cache.DashCoinDatabase.Companion.DATABASE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask


actual fun platformModule() = module {
    getDatabaseBuilder()
}

fun getDatabaseBuilder(): RoomDatabase.Builder<DashCoinDatabase> {
    val dbFilePath = documentDirectory() + "/$DATABASE_NAME"
    return Room.databaseBuilder(
        name = dbFilePath,
        factory = { DashCoinDatabase()::class.instantiateImpl() }
    )
}

private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}