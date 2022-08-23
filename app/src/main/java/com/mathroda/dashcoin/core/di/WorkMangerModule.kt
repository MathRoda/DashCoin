package com.mathroda.dashcoin.core.di

import android.app.Application
import androidx.work.WorkManager
import com.mathroda.dashcoin.domain.repository.WorkerProviderRepository
import com.mathroda.dashcoin.infrastructure.worker.WorkerProviderRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WorkMangerModule {

    @Provides
    @Singleton
    fun providesWorkManger(application: Application) =
        WorkManager.getInstance(application)

    @Provides
    @Singleton
    fun providesWorkerProviderRepository(workManager: WorkManager): WorkerProviderRepository {
        return WorkerProviderRepositoryImpl(workManager)
    }
}