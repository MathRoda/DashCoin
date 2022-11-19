package com.mathroda.favorite_coins

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WatchListScreen(
    viewModel: FavoriteCoinsViewModel = hiltViewModel(),
    navController: NavController
) {
    val isUserExists = viewModel.isCurrentUserExists.collectAsState(initial = false)

    if (isUserExists.value) {
        viewModel.refresh()
        com.mathroda.favorite_coins.components.authed_users.WatchListAuthedUsers(navController = navController)
    } else {
        com.mathroda.favorite_coins.components.ghost_users.WatchListGhostUsers(navController)
    }
}