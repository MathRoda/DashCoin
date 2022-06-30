package com.mathroda.dashcoin.presentation.coin_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.dashcoin.presentation.coin_detail.components.*
import com.mathroda.dashcoin.presentation.coin_detail.viewmodel.CoinViewModel
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen
import com.mathroda.dashcoin.presentation.ui.theme.DarkGray
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray

@Composable
fun CoinDetailScreen(
    viewModel: CoinViewModel = hiltViewModel(),
    navController: NavController
) {

    val coinState = viewModel.coinState.value
    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
            .padding(12.dp)
    ) {
       coinState.coin?.let { coin ->
           LazyColumn(
               modifier = Modifier
                   .fillMaxSize()
           ) {
               item { 
                   TopBarCoinDetail(
                       coinSymbol = coin.symbol,
                       icon = coin.icon,
                       onBackStackClicked = {navController.popBackStack()}

                   )
                   CoinDetailSection(
                       price = coin.price,
                       priceChange = coin.priceChange1d
                   )

                   Chart(
                       modifier = Modifier
                           .fillMaxWidth(),
                       oneDayChange = coin.priceChange1d,
                       context = LocalContext.current
                   )

                   CoinInformation(
                       modifier = Modifier
                           .fillMaxWidth()
                           .background(
                               color = LighterGray,
                               shape = RoundedCornerShape(25.dp)
                           )
                           .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                       rank = "${coin.rank}",
                       volume = "$"+"${coin.volume.toInt()}",
                       marketCap = "$"+"${coin.marketCap.toInt()}",
                       availableSupply = "${coin.availableSupply.toInt()} ${coin.symbol}" ,
                       totalSupply = "${coin.totalSupply.toInt()} ${coin.symbol}"
                   )

                   LinksSection()
               }
           }
       }

        if (coinState.isLoading) {
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.Center),
                color = CustomGreen
            )
        }

        if(coinState.error.isNotBlank()) {
            Text(
                text = coinState.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }
    }
}


