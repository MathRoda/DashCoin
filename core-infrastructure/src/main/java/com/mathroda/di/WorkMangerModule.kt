package com.mathroda.di

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.mathroda.datasource.providers.ProvidersRepository
import com.mathroda.notifications.CoinsNotification
import com.mathroda.notifications.CoinsNotificationChannel
import com.mathroda.workmanger.repository.WorkerProviderRepository
import com.mathroda.workmanger.repository.WorkerProviderRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WorkMangerModule {

    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Singleton
    @Provides
    fun providesCoinsNotificationChannel(context: Application):CoinsNotificationChannel {
        return CoinsNotificationChannel(context)
    }

    @Provides
    @Singleton
    fun providesCoinsNotification(
        context: Application,
        channel: CoinsNotificationChannel
    ) =
        CoinsNotification(context, channel)

    @Provides
    @Singleton
    fun providesWorkManger(application: Application) =
        WorkManager.getInstance(application)

    @Provides
    @Singleton
    fun providesWorkerProviderRepository(
        workManager: WorkManager,
        scope: CoroutineScope,
        providerRepository: ProvidersRepository
    ): WorkerProviderRepository {
        return WorkerProviderRepositoryImpl(workManager, scope, providerRepository)
    }
}