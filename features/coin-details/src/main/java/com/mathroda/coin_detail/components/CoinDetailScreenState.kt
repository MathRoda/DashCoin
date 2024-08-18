package com.mathroda.coin_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mathroda.coin_detail.CoinDetailViewModel
import com.mathroda.common.R
import com.mathroda.common.components.LoadingDots
import com.mathroda.internetconnectivity.InternetState
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import org.koin.androidx.compose.koinViewModel

@Composable
fun BoxScope.CoinDetailScreenState(
    viewModel: CoinDetailViewModel,
    coinId: String
) {
    val coinState by viewModel.coinState.collectAsState()
    val favoriteMsg = viewModel.favoriteMsg.value
    val sideEffect = viewModel.sideEffect.value
    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_main))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever,
    )
    val connectivityState by viewModel.connectivityManger.getState().collectAsState(initial = InternetState.IDLE)

    /**
     * Coin detail state:
     * isLoading, Error
     */
    when {
        coinState.isLoading -> {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                LottieAnimation(
                    composition = lottieComp,
                    progress = { lottieProgress },
                )
            }
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

    if (favoriteMsg.favoriteMessage.isNotEmpty()) {
        SweetToastUtil.SweetSuccess(
            padding = PaddingValues(24.dp),
            message = favoriteMsg.favoriteMessage
        )
    }

    /**
     * when remove favorite a coin state
     */

    if (favoriteMsg.notFavoriteMessage.isNotEmpty()) {
        SweetToastUtil.SweetError(
            padding = PaddingValues(24.dp),
            message = favoriteMsg.notFavoriteMessage
        )
    }

    /**
     * side effect state:
     * when user not authenticated yet
     */

    if (sideEffect) {
        SweetToastUtil.SweetWarning(
            padding = PaddingValues(24.dp),
            message = "Please Login First"
        )
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