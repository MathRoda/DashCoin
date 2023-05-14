package com.mathroda.favorite_coins

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.core.state.UserState
import com.mathroda.favorite_coins.components.authed_users.WatchListAuthedUsers
import com.mathroda.favorite_coins.components.ghost_users.WatchListGhostUsers
import com.mathroda.favorite_coins.components.premium_users.WatchListPremiumUsers

@ExperimentalLayoutApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun WatchListScreen(
    viewModel: FavoriteCoinsViewModel = hiltViewModel(),
    navController: NavController
) {
    when(viewModel.authState.collectAsState().value) {

        is UserState.AuthedUser -> {
            viewModel.refresh()
            WatchListAuthedUsers(navController = navController)
        }

        is UserState.UnauthedUser -> {
            WatchListGhostUsers(navController)
        }

        is UserState.PremiumUser -> {
            viewModel.refresh()
            WatchListPremiumUsers(navController = navController)
        }
    }

    LaunchedEffect(true) {
        viewModel.init()
    }
}