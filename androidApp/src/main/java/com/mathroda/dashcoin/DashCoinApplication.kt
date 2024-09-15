package com.mathroda.dashcoin

import android.app.Application
import com.mathroda.shared.di.initKoin
import com.mathroda.workmanger.WorkerProviderRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory

class DashCoinApplication : Application() {
    private val workerProviderRepository: WorkerProviderRepository by inject()
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@DashCoinApplication)
            workManagerFactory()
        }
        workerProviderRepository.createWork()
    }
}