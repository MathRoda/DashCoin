package com.mathroda.common.navigation

import com.mathroda.common.R

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: Int? = null
) {
    data object CoinsScreen : Destinations(
        route = "coins_screen",
        title = "Home",
        icon = R.drawable.ic_crypto
    )

    data object FavoriteCoinsScreen : Destinations(
        route = "coins_watch_list",
        title = "WatchList",
        icon = R.drawable.ic_heart
    )

    data object CoinsNews : Destinations(
        route = "coins_news",
        title = "News",
        icon = R.drawable.ic_news
    )

    data object CoinDetailScreen : Destinations("coin_detail_screen")
    data object SignUp : Destinations(route = "sign_up")
    data object SignIn : Destinations(route = "sign_in")
    data object ForgotPassword : Destinations(route = "forgot_password")

    data object Settings: Destinations(route = "settings")


}
