package com.mathroda.dashcoin.presentation.coin_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mathroda.dashcoin.presentation.ui.theme.TextWhite

@Composable
fun TopBarCoinDetail(
    coinSymbol: String,
    icon: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {

        AsyncImage(
            model = icon ,
            contentDescription = "Icon",
            modifier = Modifier
                .size(30.dp)
                .padding(end = 5.dp)
        )

        Text(
            text = coinSymbol,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h2,
            color = TextWhite
        )
    }
}