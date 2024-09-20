package com.mathroda.coin_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.TextWhite

@Composable
fun CoinInformation(
    modifier: Modifier = Modifier,
    volume: String,
    marketCap: String,
    availableSupply: String,
    totalSupply: String,
    rank: String
) {
    Column(modifier = modifier) {
        CoinInfoRow(value = rank, title = "Rank")
        CoinInfoRow(value = marketCap, title = "Market cap")
        CoinInfoRow(value = volume, title = "Volume")
        CoinInfoRow(value = availableSupply, title = "Available supply")
        CoinInfoRow(value = totalSupply, title = "Total supply")
    }
}

@Composable
fun CoinInfoRow(
    value: String,
    title: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = value,
            color = TextWhite,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.body2
        )

    }
}
