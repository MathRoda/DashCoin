package com.mathroda.dashcoin.di

import com.mathroda.OnBoardingViewModel
import com.mathroda.signup.SignUpViewModel
import com.mathroda.coin_details.CoinDetailViewModel
import com.mathroda.coins_screen.CoinsViewModel
import com.mathroda.dashcoin.splash.SplashViewModel
import com.mathroda.favorite_coins.FavoriteCoinsViewModel
import com.mathroda.ResetPasswordViewModel
import com.mathroda.news_screen.NewsViewModel
import com.mathroda.profile_screen.ProfileViewModel
import com.mathroda.profile_screen.settings.SettingViewModel
import com.mathroda.signin_screen.SignInViewModel
import org.koin.dsl.module

val viewModelsModule = module {
    factory { ResetPasswordViewModel(get()) }
    factory { SignInViewModel(get(),get(),get(),get()) }
    factory { SignUpViewModel(get(),get()) }
    factory { CoinDetailViewModel(get(),get(), get()) }
    factory { CoinsViewModel(get(), get()) }
    factory { FavoriteCoinsViewModel(get(), get(), get()) }
    factory { NewsViewModel(get(), get()) }
    factory { OnBoardingViewModel(get()) }
    factory { ProfileViewModel(get(), get(), get(), get()) }
    factory { SplashViewModel(get(), get(), get(), get(), get()) }
    factory { SettingViewModel(get()) }
}