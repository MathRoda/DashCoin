package com.mathroda.favorite_coins.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathroda.common.components.shimmerEffect
import com.mathroda.common.theme.LighterGray

@Composable
fun MarketStatusBar(
    modifier: Modifier = Modifier,
    marketStatus1h: Double,
    marketStatus1d: Double,
    marketStatus1w: Double,
    isLoading: Boolean = false
    ) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        MarketStatusItem(
            title = "1h",
            marketStatus = marketStatus1h,
            isLoading = isLoading,
            modifier = Modifier
                .weight(1f)
                .shadow(4.dp)
                .background(
                    color = LighterGray,
                    shape = RoundedCornerShape(25),
                )

        )

        MarketStatusItem(
            title = "1d",
            marketStatus = marketStatus1d,
            isLoading = isLoading,
            modifier = Modifier
                .weight(1f)
                .shadow(4.dp)
                .background(
                    color = LighterGray,
                    shape = RoundedCornerShape(25),
                )
        )

        MarketStatusItem(
            title = "1w",
            marketStatus = marketStatus1w,
            isLoading = isLoading,
            modifier = Modifier
                .weight(1f)
                .shadow(4.dp)
                .background(
                    color = LighterGray,
                    shape = RoundedCornerShape(25),
                )
        )

    }
}

@Composable
fun MarketStatusItem(
    modifier: Modifier = Modifier,
    title: String = "",
    marketStatus: Double = 0.0,
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h4,
                color = com.mathroda.common.theme.TextWhite,
            )
        }

        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (isLoading) {
                Text(
                    text = "0.0%",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.shimmerEffect()
                )
            } else {
                Image(
                    painter = if (marketStatus < 0) painterResource(id = com.mathroda.common.R.drawable.ic_arrow_negative)
                    else painterResource(id = com.mathroda.common.R.drawable.ic_arrow_positive),
                    contentDescription = null,
                    modifier = Modifier
                        .size(12.dp)
                        .padding(end = 4.dp)
                )

                Text(
                    text = "$marketStatus%",
                    style = MaterialTheme.typography.body2,
                    color = if (marketStatus < 0) com.mathroda.common.theme.CustomRed else com.mathroda.common.theme.CustomGreen,
                    modifier = Modifier
                )
            }
        }
    }
}