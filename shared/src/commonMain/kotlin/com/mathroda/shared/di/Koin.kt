package com.mathroda.shared.di

import com.mathroda.cache.di.cacheModule
import com.mathroda.core.di.coreModule
import com.mathroda.datasource.di.dataSourceModule
import com.mathroda.di.infrastructureModule
import com.mathroda.network.di.networkModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(
    declaration: KoinAppDeclaration = {}
) {
    startKoin {
        declaration()
        modules(
            coreModule,
            cacheModule,
            networkModule,
            dataSourceModule,
            infrastructureModule,
            viewModelsModule
        )
    }
}

fun initKoin() = initKoin {  }
