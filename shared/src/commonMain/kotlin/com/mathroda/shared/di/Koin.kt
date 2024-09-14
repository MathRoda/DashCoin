package com.mathroda.shared.di

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.mathroda.cache.di.cacheModule
import com.mathroda.datasource.di.dataSourceModule
import com.mathroda.di.infrastructureModule
import com.mathroda.network.di.networkModule
import com.mathroda.shared.ui.screens.coinDetailsScreen
import com.mathroda.shared.ui.screens.coinsScreen
import com.mathroda.shared.ui.screens.favoriteCoinsScreen
import com.mathroda.shared.ui.screens.forgotPasswordScreen
import com.mathroda.shared.ui.screens.newsScreen
import com.mathroda.shared.ui.screens.onboardingScreen
import com.mathroda.shared.ui.screens.settingsScreen
import com.mathroda.shared.ui.screens.signInScreen
import com.mathroda.shared.ui.screens.signUpScreen
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(
    declaration: KoinAppDeclaration = {}
) {
    startKoin {
        declaration()
        modules(
            cacheModule,
            networkModule,
            dataSourceModule,
            infrastructureModule,
            viewModelsModule
        )
    }
}

fun registerScreens() {
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