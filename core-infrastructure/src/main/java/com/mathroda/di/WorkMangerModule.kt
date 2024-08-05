package com.mathroda.di

import androidx.work.WorkManager
import com.mathroda.internetconnectivity.InternetConnectivityManger
import com.mathroda.internetconnectivity.InternetConnectivityMangerImpl
import com.mathroda.notifications.coins.CoinsNotification
import com.mathroda.notifications.coins.CoinsNotificationChannel
import com.mathroda.notifications.sync.SyncNotification
import com.mathroda.notifications.sync.SyncNotificationChannel
import com.mathroda.phoneshaking.PhoneShakingManger
import com.mathroda.phoneshaking.PhoneShakingMangerImpl
import com.mathroda.workmanger.repository.WorkerProviderRepository
import com.mathroda.workmanger.repository.WorkerProviderRepositoryImpl
import com.mathroda.workmanger.worker.DashCoinWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val infrastructureModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    single { CoinsNotificationChannel(androidContext()) }
    single { SyncNotificationChannel(androidContext()) }
    single { CoinsNotification(androidApplication(), channel = get()) }
    single { SyncNotification(androidApplication(), get()) }
    single { WorkManager.getInstance(androidApplication()) }
    worker { DashCoinWorker(androidApplication(), get(), get(), get(), get()) }
    single<WorkerProviderRepository> {
        WorkerProviderRepositoryImpl(
            get(), get(), get(), get()
        )
    }

    single<PhoneShakingManger> { PhoneShakingMangerImpl(androidApplication()) }

    single<InternetConnectivityManger> {
        InternetConnectivityMangerImpl(androidApplication())
    }
}