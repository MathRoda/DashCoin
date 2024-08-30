package com.mathroda.di

import com.mathroda.internetconnectivity.InternetConnectivityManger
import com.mathroda.internetconnectivity.InternetConnectivityMangerImpl
import com.mathroda.notifications.coins.CoinsNotification
import com.mathroda.notifications.sync.SyncNotification
import com.mmk.kmpnotifier.notification.NotifierManager
import com.plusmobileapps.konnectivity.Konnectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val infrastructureModule = module {
    includes(platformModule())
    single { NotifierManager.getLocalNotifier() }
    single { CoinsNotification(get()) }
    single { SyncNotification(get()) }
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single { Konnectivity() }
    single<InternetConnectivityManger> { InternetConnectivityMangerImpl(get()) }
}