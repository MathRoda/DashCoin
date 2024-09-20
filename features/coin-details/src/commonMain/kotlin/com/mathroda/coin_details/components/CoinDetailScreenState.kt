package com.mathroda.coin_details.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mathroda.coin_details.CoinDetailViewModel
import com.mathroda.common.components.LoadingCrypto
import com.mathroda.common.components.LoadingDots
import com.mathroda.common.toastmessage.components.LocalMessageBar
import com.mathroda.internetconnectivity.InternetState

@Composable
fun BoxScope.CoinDetailScreenState(
    viewModel: CoinDetailViewModel,
    coinId: String
) {
    val coinState by viewModel.coinState.collectAsState()
    val favoriteMsg = viewModel.favoriteMsg.value
    val sideEffect = viewModel.sideEffect.value
    val connectivityState by viewModel.connectivityManger.getState().collectAsState(InternetState.IDLE)
    val messageBarState = LocalMessageBar.current

    /**
     * Coin detail state:
     * isLoading, Error
     */
    when {
        coinState.isLoading -> {
            LoadingCrypto()
        }

        coinState.error.isNotBlank() -> {

            Text(
                text = coinState.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )

            if(connectivityState is InternetState.Available) {
                viewModel.updateUiState(coinId)
            }
        }
    }


    /**
     * when add favorite a coin state
     */

    LaunchedEffect(favoriteMsg.favoriteMessage) {
        if (favoriteMsg.favoriteMessage.isNotEmpty()) {
            messageBarState.showSuccess(
                message = favoriteMsg.favoriteMessage
            )
        }
    }

    /**
     * when remove favorite a coin state
     */

    LaunchedEffect(favoriteMsg.notFavoriteMessage) {
        if (favoriteMsg.notFavoriteMessage.isNotEmpty()) {
            messageBarState.showError(
                message = favoriteMsg.notFavoriteMessage
            )
        }
    }

    /**
     * side effect state:
     * when user not authenticated yet
     */

    LaunchedEffect(sideEffect) {
        if (sideEffect) {
            messageBarState.showError(
                message = "Please Login First"
            )
        }
    }

}

/**
 * chart state :
 * isLoading
 */
@Composable
fun LoadingChartState(
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingDots(isLoading = isLoading)
    }
}