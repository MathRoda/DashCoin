package com.mathroda.shared.di

import com.mathroda.ResetPasswordViewModel
import com.mathroda.coin_details.CoinDetailViewModel
import com.mathroda.coins.CoinsViewModel
import com.mathroda.favorites.FavoriteCoinsViewModel
import com.mathroda.news.NewsViewModel
import com.mathroda.onboarding.OnBoardingViewModel
import com.mathroda.profile.ProfileViewModel
import com.mathroda.profile.settings.SettingViewModel
import com.mathroda.shared.SharedScreenModel
import com.mathroda.signin_screen.SignInViewModel
import com.mathroda.signup.SignUpViewModel
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
    single { SharedScreenModel(get(), get(), get(), get(), get()) }
    factory { SettingViewModel(get(), get()) }
}