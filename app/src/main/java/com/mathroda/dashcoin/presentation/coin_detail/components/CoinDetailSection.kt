package com.mathroda.dashcoin.presentation.coin_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen
import com.mathroda.dashcoin.presentation.ui.theme.CustomRed
import com.mathroda.dashcoin.presentation.ui.theme.TextWhite

@Composable
fun CoinDetailSection(
    price: Double,
    priceChange: Double
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = "$" + price.toFloat(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h1,
                color = TextWhite
            )
            Text(
                text = "$priceChange%",
                style = MaterialTheme.typography.body1,
                color = if (priceChange < 0) CustomRed else CustomGreen
            )
        }
    }
}