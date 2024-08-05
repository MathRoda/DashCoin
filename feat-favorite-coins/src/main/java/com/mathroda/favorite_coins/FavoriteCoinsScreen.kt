package com.mathroda.favorite_coins

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.mathroda.common.components.DeleteAllCoinsDialog
import com.mathroda.common.state.DialogState
import com.mathroda.core.state.UserState
import com.mathroda.favorite_coins.components.authed_users.WatchListAuthedUsers
import com.mathroda.favorite_coins.components.ghost_users.WatchListGhostUsers
import com.mathroda.favorite_coins.components.premium_users.WatchListPremiumUsers
import com.mathroda.phoneshaking.PhoneShakingState
import org.koin.androidx.compose.koinViewModel

@ExperimentalLayoutApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun WatchListScreen(
    navController: NavController
) {
    val viewModel = koinViewModel<FavoriteCoinsViewModel>()
    val dialogState by viewModel.dialogState.collectAsState()
    val phoneShakingState by viewModel.phoneShakingManger.getState().collectAsState(initial = PhoneShakingState.IDLE)
    val userState by viewModel.authState.collectAsState()

    when(userState) {

        is UserState.AuthedUser -> {
            WatchListAuthedUsers(navController = navController)
            if (phoneShakingState is PhoneShakingState.IsShaking) {
                viewModel.updateDeleteAllDialog(DialogState.Open)
            }
        }

        is UserState.UnauthedUser -> {
            WatchListGhostUsers(navController)
        }

        is UserState.PremiumUser -> {
            WatchListPremiumUsers(navController = navController)
            if (phoneShakingState is PhoneShakingState.IsShaking) {
                viewModel.updateDeleteAllDialog(DialogState.Open)
            }
        }
    }

    DeleteAllCoinsDialog(
        dialogState = dialogState,
        onDialogStateChanged = viewModel::updateDeleteAllDialog
    ) {
        viewModel.deleteAllFavoriteCoins()
    }

    LaunchedEffect(true) {
        viewModel.init()
    }
}