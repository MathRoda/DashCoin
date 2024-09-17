package com.mathroda.shared.destination

import com.mathroda.common.resources.Res
import com.mathroda.common.resources.ic_crypto
import com.mathroda.common.resources.ic_heart
import com.mathroda.common.resources.ic_news
import org.jetbrains.compose.resources.DrawableResource

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: DrawableResource? = null
) {
    data object CoinsScreen : Destinations(
        route = "coins_screen",
        title = "Home",
        icon = Res.drawable.ic_crypto
    )

    data object FavoriteCoinsScreen : Destinations(
        route = "coins_watch_list",
        title = "WatchList",
        icon = Res.drawable.ic_heart
    )

    data object CoinsNews : Destinations(
        route = "coins_news",
        title = "News",
        icon = Res.drawable.ic_news
    )

    data object CoinDetailScreen : Destinations("coin_detail_screen")
    data object SignUp : Destinations(route = "sign_up")
    data object SignIn : Destinations(route = "sign_in")
    data object ForgotPassword : Destinations(route = "forgot_password")
    data object Settings: Destinations(route = "settings")
    data object Onboarding: Destinations(route = "onboarding")


}