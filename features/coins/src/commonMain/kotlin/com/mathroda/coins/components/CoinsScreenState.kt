package com.mathroda.coins.components

import androidx.compose.foundation.layout.BoxScope
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
import com.mathroda.coins.CoinsViewModel
import com.mathroda.common.components.LoadingCrypto
import com.mathroda.internetconnectivity.InternetState

@Composable
fun BoxScope.CoinsScreenState(
    viewModel: CoinsViewModel
) {
    val state = viewModel.state.collectAsState().value
    val connectivityState by viewModel.connectivityManger.getState().collectAsState(InternetState.IDLE)

    when {
        state.isLoading -> {
            LoadingCrypto()
        }

        state.error.isNotEmpty() -> {

            Text(
                text = state.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )

            if(connectivityState is InternetState.Available) {
                viewModel.refresh()
            }
        }
    }

}