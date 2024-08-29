package com.mathroda.dashcoin

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.mathroda.cache.di.cacheModule
import com.mathroda.dashcoin.di.viewModelsModule
import com.mathroda.dashcoin.ui.screens.coinDetailsScreen
import com.mathroda.dashcoin.ui.screens.coinsScreen
import com.mathroda.dashcoin.ui.screens.favoriteCoinsScreen
import com.mathroda.dashcoin.ui.screens.forgotPasswordScreen
import com.mathroda.dashcoin.ui.screens.newsScreen
import com.mathroda.dashcoin.ui.screens.onboardingScreen
import com.mathroda.dashcoin.ui.screens.settingsScreen
import com.mathroda.dashcoin.ui.screens.signInScreen
import com.mathroda.dashcoin.ui.screens.signUpScreen
import com.mathroda.datasource.di.dataSourceModule
import com.mathroda.di.infrastructureModule
import com.mathroda.network.di.networkModule
import com.mathroda.workmanger.WorkerProviderRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class DashCoinApplication : Application() {
    private val workerProviderRepository: WorkerProviderRepository by inject()
    override fun onCreate() {
        super.onCreate()
        registerScreens()
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

    private fun registerScreens() {
        ScreenRegistry {
            onboardingScreen()
            coinsScreen()
            favoriteCoinsScreen()
            newsScreen()
            signInScreen()
            signUpScreen()
            forgotPasswordScreen()
            coinDetailsScreen()
            settingsScreen()
        }
    }
}