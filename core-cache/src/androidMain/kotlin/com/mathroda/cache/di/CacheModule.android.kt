package com.mathroda.cache.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mathroda.cache.DashCoinDatabase
import com.mathroda.cache.DashCoinDatabase.Companion.DATABASE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        getDatabaseBuilder(androidContext())
    }
}

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<DashCoinDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<DashCoinDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
