package com.mathroda.di

import com.mathroda.internetconnectivity.InternetConnectivityImpl
import com.mathroda.internetconnectivity.InternetConnectivityManger
import com.mathroda.phoneshaking.PhoneShakingManagerImpl
import com.mathroda.phoneshaking.PhoneShakingManger
import org.koin.dsl.module

actual fun platformModule() = module {
    single<InternetConnectivityManger> { InternetConnectivityImpl() }
    single<PhoneShakingManger> { PhoneShakingManagerImpl() }
}