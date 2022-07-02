package com.mathroda.dashcoin.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(
    val route: String,
    val title: String?= null,
    val icon: ImageVector?= null
    ) {
    object CoinsScreen: Screens(
        route = "coins_screen",
        title = "Home",
        icon = Icons.Default.Home
    )

    object CoinsWatchList: Screens(
        route = "coins_watch_list",
        title = "WatchList",
        icon = Icons.Default.Favorite
    )

    object CoinsNews: Screens(
        route = "coins_news",
        title = "News",
        icon = Icons.Default.List
    )
    object CoinDetailScreen: Screens("coin_detail_screen")
}
