@file:OptIn(ExperimentalResourceApi::class)

package com.mathroda.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mathroda.common.components.DeleteAllCoinsDialog
import com.mathroda.core.state.DialogState
import com.mathroda.core.state.UserState
import com.mathroda.favorites.components.authed_users.WatchListAuthedUsers
import com.mathroda.favorites.components.ghost_users.WatchListGhostUsers
import com.mathroda.favorites.components.premium_users.WatchListPremiumUsers
import com.mathroda.phoneshaking.PhoneShakingState
import org.jetbrains.compose.resources.ExperimentalResourceApi

@ExperimentalLayoutApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun WatchListScreen(
    viewModel: FavoriteCoinsViewModel,
    navigateToCoinDetails: (String) -> Unit,
    navigateToSignIn: () -> Unit
) {
    val dialogState by viewModel.dialogState.collectAsState()
    val phoneShakingState by viewModel.phoneShakingManger.getState().collectAsState(initial = PhoneShakingState.IDLE)
    val userState by viewModel.authState.collectAsState()

    when(userState) {
        is UserState.AuthedUser -> {
            WatchListAuthedUsers(
                viewModel = viewModel,
                navigateToCoinDetails = navigateToCoinDetails
            )
            if (phoneShakingState is PhoneShakingState.IsShaking) {
                viewModel.updateDeleteAllDialog(DialogState.Open)
            }
        }

        is UserState.UnauthedUser -> {
            WatchListGhostUsers(navigateToSignIn)
        }

        is UserState.PremiumUser -> {
            WatchListPremiumUsers(
                viewModel = viewModel,
                navigateToCoinDetails = navigateToCoinDetails
            )
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