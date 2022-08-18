package com.mathroda.dashcoin.navigation.graphs

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mathroda.dashcoin.navigation.main.Screens
import com.mathroda.dashcoin.presentation.coin_detail.CoinDetailScreen
import com.mathroda.dashcoin.presentation.coins_screen.CoinScreen
import com.mathroda.dashcoin.presentation.news_screen.NewsScreen
import com.mathroda.dashcoin.presentation.watchlist_screen.WatchListScreen

@ExperimentalMaterialApi
@Composable
fun MainGraph(navController: NavHostController) {
    
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = Screens.CoinsScreen.route
    ) {
        composable(
            route = Screens.CoinsScreen.route
        ){
            CoinScreen(navController = navController)
        }

        composable(
            route = Screens.CoinsWatchList.route
        ){
            WatchListScreen(navController = navController)
        }

        composable(
            route = Screens.CoinsNews.route
        ){
            NewsScreen(navController = navController)
        }

        composable(
            route = Screens.CoinDetailScreen.route + "/{coinId}"
        ){
            CoinDetailScreen(navController = navController)
        }
    }
}