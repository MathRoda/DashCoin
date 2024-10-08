package com.mathroda.news.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.CustomGreen
import com.mathroda.internetconnectivity.InternetState
import com.mathroda.news.NewsViewModel

@Composable
fun BoxScope.NewsScreenState(
    viewModel: NewsViewModel
) {
    val state = viewModel.newsState.value
    val connectivityState by viewModel.connectivityManger.getState().collectAsState(InternetState.IDLE)

    when {
        state.isLoading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                color = CustomGreen
            )
        }
        state.error.isNotBlank() -> {
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