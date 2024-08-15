package com.mathroda.dashcoin.di

import com.mathroda.OnBoardingViewModel
import com.mathroda.SignUpViewModel
import com.mathroda.coin_detail.CoinDetailViewModel
import com.mathroda.coins_screen.CoinsViewModel
import com.mathroda.dashcoin.splash.SplashViewModel
import com.mathroda.favorite_coins.FavoriteCoinsViewModel
import com.mathroda.forgot_password.ResetPasswordViewModel
import com.mathroda.news_screen.NewsViewModel
import com.mathroda.profile_screen.ProfileViewModel
import com.mathroda.profile_screen.settings.SettingViewModel
import com.mathroda.signin_screen.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { ResetPasswordViewModel(get()) }
    viewModel { SignInViewModel(get(),get(),get(),get()) }
    viewModel { SignUpViewModel(get(),get()) }
    viewModel { CoinDetailViewModel(get(),get(), get()) }
    viewModel { CoinsViewModel(get(), get()) }
    viewModel { FavoriteCoinsViewModel(get(), get(), get()) }
    viewModel { NewsViewModel(get(), get()) }
    viewModel { OnBoardingViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel(get(), get(), get(), get(), get()) }
    viewModel { SettingViewModel(get()) }
}