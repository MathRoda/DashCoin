package com.mathroda.infrastructure.di

import android.app.Application
import androidx.work.WorkManager
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.infrastructure.repository.WorkerProviderRepository
import com.mathroda.infrastructure.repository.WorkerProviderRepositoryImpl
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
    fun providesWorkerProviderRepository(
        workManager: WorkManager,
        firebaseRepository: FirebaseRepository
    ): WorkerProviderRepository {
        return WorkerProviderRepositoryImpl(workManager, firebaseRepository)
    }
}