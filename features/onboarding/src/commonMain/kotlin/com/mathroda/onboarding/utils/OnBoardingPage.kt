package com.mathroda.onboarding.utils

import com.mathroda.common.resources.Res
import com.mathroda.common.resources.ic_onboarding_first
import com.mathroda.common.resources.ic_onboarding_second
import com.mathroda.common.resources.ic_onboarding_third
import org.jetbrains.compose.resources.DrawableResource

sealed class OnBoardingPage(
    val image: DrawableResource,
    val title: String,
    val description: String
) {
    data object FirstScreen : OnBoardingPage(
        image = Res.drawable.ic_onboarding_second,
        title = "Top Coins",
        description = "get access to all information's about the top coins in the market"
    )

    data object SecondScreen : OnBoardingPage(
        image = Res.drawable.ic_onboarding_first,
        title = "Be Borderless",
        description = "tracking your favorite coins allows you to get custom notifications about the coin and a lot more"
    )

    data object ThirdScreen : OnBoardingPage(
        image = Res.drawable.ic_onboarding_third,
        title = "Hottest News",
        description = "You have access to the hottest and newest news about the crypto market"
    )
}
