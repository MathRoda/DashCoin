package com.mathroda.dashcoin

import android.app.Application
import com.example.cache.di.cacheModule
import com.mathroda.dashcoin.di.viewModelsModule
import com.mathroda.datasource.di.dataSourceModule
import com.mathroda.di.infrastructureModule
import com.mathroda.network.di.networkModule
import com.mathroda.workmanger.repository.WorkerProviderRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class DashCoinApplication : Application() {
    private val workerProviderRepository: WorkerProviderRepository by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DashCoinApplication)
            workManagerFactory()
            modules(
                cacheModule,
                networkModule,
                dataSourceModule,
                infrastructureModule,
                viewModelsModule
            )
        }

        workerProviderRepository.createWork()
    }

}