package com.mathroda.datasource.di

import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.providers.ProvidersRepository
import com.mathroda.datasource.providers.ProvidersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ProvidersModule {


    @Provides
    @Singleton
    fun providesProvidersRepository(
        firebaseRepository: FirebaseRepository
    ): ProvidersRepository {
        return ProvidersRepositoryImpl(firebaseRepository)
    }

}