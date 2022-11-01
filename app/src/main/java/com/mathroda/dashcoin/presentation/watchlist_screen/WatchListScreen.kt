package com.mathroda.dashcoin.presentation.watchlist_screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.dashcoin.presentation.watchlist_screen.authed_users.WatchListAuthedUsers
import com.mathroda.dashcoin.presentation.watchlist_screen.ghost_users.WatchListGhostUsers
import com.mathroda.dashcoin.presentation.watchlist_screen.viewmodel.WatchListViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WatchListScreen(
    viewModel: WatchListViewModel = hiltViewModel(),
    navController: NavController
) {
    val isUserExists = viewModel.isCurrentUserExists.collectAsState(initial = false)

    if (isUserExists.value) {
        viewModel.refresh()
        WatchListAuthedUsers(navController = navController)
    } else {
        WatchListGhostUsers(navController)
    }
}