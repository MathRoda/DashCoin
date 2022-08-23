package com.mathroda.dashcoin.navigation.graphs

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mathroda.dashcoin.navigation.auth.authNavGraph

import com.mathroda.dashcoin.navigation.main.MainScreen
import com.mathroda.dashcoin.navigation.spalsh.splashNavGraph

@ExperimentalMaterialApi
@Composable
fun RootNavigationGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.SPLASH
    ) {
        splashNavGraph(navHostController)
        authNavGraph(navHostController)
        composable(route = Graph.MAIN) {
            MainScreen()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val SPLASH ="splash_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
}