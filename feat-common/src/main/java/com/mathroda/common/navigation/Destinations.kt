package com.mathroda.common.navigation

import com.mathroda.common.R

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: Int? = null
) {
    object CoinsScreen : Destinations(
        route = "coins_screen",
        title = "Home",
        icon = R.drawable.ic_crypto
    )

    object FavoriteCoinsScreen : Destinations(
        route = "coins_watch_list",
        title = "WatchList",
        icon = R.drawable.ic_heart
    )

    object CoinsNews : Destinations(
        route = "coins_news",
        title = "News",
        icon = R.drawable.ic_news
    )

    object CoinDetailScreen : Destinations("coin_detail_screen")
    object SignUp : Destinations(route = "sign_up")
    object SignIn : Destinations(route = "sign_in")
    object ForgotPassword : Destinations(route = "forgot_password")

}
