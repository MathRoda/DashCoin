package com.mathroda.common.navigation

import com.mathroda.common.R

sealed class Screens(
    val route: String,
    val title: String?= null,
    val icon: Int?= null
    ) {
    object CoinsScreen: Screens(
        route = "coins_screen",
        title = "Home",
        icon = R.drawable.ic_crypto
    )

    object CoinsWatchList: Screens(
        route = "coins_watch_list",
        title = "WatchList",
        icon = R.drawable.ic_heart
    )

    object CoinsNews: Screens(
        route = "coins_news",
        title = "News",
        icon = R.drawable.ic_news
    )
    object CoinDetailScreen: Screens("coin_detail_screen")
    object SignUp: Screens(route = "sign_up")
    object SignIn: Screens(route = "sign_in")
    object ForgotPassword: Screens(route = "forgot_password")

}
