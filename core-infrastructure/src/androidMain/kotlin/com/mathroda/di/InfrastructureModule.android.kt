package com.mathroda.di

import androidx.work.WorkManager
import com.mathroda.infrastructure.R
import com.mathroda.internetconnectivity.InternetConnectivityManger
import com.mathroda.internetconnectivity.InternetConnectivityMangerImpl
import com.mathroda.notifications.coins.CoinsNotification
import com.mathroda.notifications.sync.SyncNotification
import com.mathroda.phoneshaking.PhoneShakingManger
import com.mathroda.phoneshaking.PhoneShakingMangerImpl
import com.mathroda.workmanger.WorkerProviderRepository
import com.mathroda.workmanger.repository.WorkerProviderRepositoryImpl
import com.mathroda.workmanger.worker.DashCoinWorker
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

actual fun platformModule() = module {
    single<InternetConnectivityManger> { InternetConnectivityMangerImpl(androidApplication()) }
    single<PhoneShakingManger> { PhoneShakingMangerImpl(androidApplication()) }

    single {
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = com.mathroda.common.R.drawable.ic_crypto
            )
        )
    }
    single { WorkManager.getInstance(androidApplication()) }
    worker { DashCoinWorker(androidApplication(), get(), get(), get(), get()) }

    single<WorkerProviderRepository> {
        WorkerProviderRepositoryImpl(
            get(), get(), get(), get()
        )
    }
}