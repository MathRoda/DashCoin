package com.mathroda.di

import com.mathroda.internetconnectivity.InternetConnectivityManger
import com.mathroda.internetconnectivity.InternetConnectivityMangerImpl
import com.mathroda.phoneshaking.PhoneShakingManagerImpl
import com.mathroda.phoneshaking.PhoneShakingManger
import org.koin.dsl.module

actual fun platformModule() = module {
    single<PhoneShakingManger> { PhoneShakingManagerImpl() }
}