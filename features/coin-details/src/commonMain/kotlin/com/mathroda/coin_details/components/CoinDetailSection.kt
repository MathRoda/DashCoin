package com.mathroda.coin_details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathroda.common.resources.Res
import com.mathroda.common.resources.ic_arrow_negative
import com.mathroda.common.resources.ic_arrow_positive
import com.mathroda.common.theme.CustomGreen
import com.mathroda.common.theme.CustomRed
import com.mathroda.common.theme.LighterGray
import com.mathroda.common.theme.TextWhite
import org.jetbrains.compose.resources.painterResource

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
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = "$${price.toFloat()}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h1,
                color = TextWhite
            )

            Row(
                Modifier.padding(horizontal = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(LighterGray)
                        .size(21.dp)
                ) {
                    Text(
                        text = "24h",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                Text(
                    text = "$priceChange%",
                    style = MaterialTheme.typography.body1,
                    color = if (priceChange < 0) CustomRed else CustomGreen
                )

                Image(
                    painter = if (priceChange < 0) {
                        painterResource(Res.drawable.ic_arrow_negative)
                    } else {
                        painterResource(Res.drawable.ic_arrow_positive)
                    },
                    contentDescription = "arrow",
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                )
            }

        }
    }
}