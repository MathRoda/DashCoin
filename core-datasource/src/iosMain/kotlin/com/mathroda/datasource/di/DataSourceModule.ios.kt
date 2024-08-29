@file:OptIn(ExperimentalForeignApi::class)

package com.mathroda.datasource.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mathroda.datasource.datastore.createDataStore
import com.mathroda.datasource.datastore.dataStoreFileName
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual fun platformModule() = module {
    single { initDataStore() }
}

fun initDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(documentDirectory).path + "/$dataStoreFileName"
    }
)
