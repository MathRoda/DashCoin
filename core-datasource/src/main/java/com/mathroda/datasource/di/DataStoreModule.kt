package com.mathroda.datasource.di

import android.content.Context
import com.mathroda.datasource.datastore.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun  providesDataStoreRepository(
        @ApplicationContext context: Context
    ) = DataStoreRepository(context)
}