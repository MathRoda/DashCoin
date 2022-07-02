package com.mathroda.dashcoin.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mathroda.dashcoin.presentation.coin_detail.CoinDetailScreen
import com.mathroda.dashcoin.presentation.coins_screen.CoinScreen
import com.mathroda.dashcoin.presentation.news_screen.NewsScreen
import com.mathroda.dashcoin.presentation.watchlist_screen.WatchListScreen

@ExperimentalMaterialApi
@Composable
fun BottomNavGraph(navController: NavHostController) {
    
    NavHost(
        navController = navController,
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