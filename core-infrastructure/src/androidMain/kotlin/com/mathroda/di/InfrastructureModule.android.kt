package com.mathroda.di

import androidx.work.WorkManager
import com.mathroda.internetconnectivity.InternetConnectivityManagerImpl
import com.mathroda.internetconnectivity.InternetConnectivityManger
import com.mathroda.phoneshaking.PhoneShakingManger
import com.mathroda.phoneshaking.PhoneShakingMangerImpl
import com.mathroda.workmanger.WorkerProviderRepository
import com.mathroda.workmanger.worker.DashCoinWorker
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import kotlin.math.sin

actual fun platformModule() = module {
    single<PhoneShakingManger> { PhoneShakingMangerImpl(androidApplication()) }
    single { WorkManager.getInstance(androidApplication()) }
    worker { DashCoinWorker(androidApplication(), get(), get(), get(), get()) }
    single<WorkerProviderRepository> {
        WorkerProviderRepository(
            get(), get(), get(), get()
        )
    }

    single<InternetConnectivityManger> { InternetConnectivityManagerImpl(androidApplication()) }
}