package com.mathroda.utils

import androidx.annotation.DrawableRes
import com.mathroda.onboarding.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object FirstScreen : OnBoardingPage(
        image = R.drawable.ic_onboarding_second,
        title = "Top Coins",
        description = "get access to all information's about the top coins in the market"
    )

    object SecondScreen : OnBoardingPage(
        image = R.drawable.ic_onboarding_first,
        title = "Be Borderless",
        description = "tracking your favorite coins allows you to get custom notifications about the coin and a lot more"
    )

    object ThirdScreen : OnBoardingPage(
        image = R.drawable.ic_onboarding_third,
        title = "Hottest News",
        description = "You have access to the hottest and newest news about the crypto market"
    )
}
