package com.mathroda.dashcoin

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.mathroda.dashcoin.domain.repository.WorkerProviderRepository
import com.mathroda.dashcoin.domain.use_case.worker.CreateWorkUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DashCoinApplication : Application(), Configuration.Provider {

    @Inject
     lateinit var workerFactory: HiltWorkerFactory

    @Inject
     lateinit var createWorkUseCase: CreateWorkUseCase

    override fun onCreate() {
        super.onCreate()
        createWorkUseCase.invoke()
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}