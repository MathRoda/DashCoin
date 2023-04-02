package com.mathroda.coins_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun CoinsItem(
    coins: com.mathroda.domain.Coins,
    onItemClick: (com.mathroda.domain.Coins) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(coins) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(2f)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(com.mathroda.common.theme.LighterGray)
                        .size(50.dp)
                ) {

                    AsyncImage(
                        model = coins.icon,
                        contentDescription = "Icon",
                        modifier = Modifier
                            .size(20.dp)
                            .align(Center)
                    )
                }
            }



            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(5f)
            ) {
                Text(
                    text = coins.name,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                    color = com.mathroda.common.theme.TextWhite,
                    textAlign = TextAlign.Start
                )

                Row {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .padding(0.dp)
                            .background(com.mathroda.common.theme.LighterGray)
                            .align(CenterVertically)
                            .padding(2.dp)
                    ) {
                        Text(
                            text = coins.rank.toString(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.mathroda.common.theme.Gold,
                            modifier = Modifier
                                .align(Center)
                        )
                    }
                    Text(
                        text = coins.symbol,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(start = 5.dp)
                    )

                }

            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(3f)
            ) {
                Text(
                    text = "$" + coins.price.toFloat().toString(),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    color = com.mathroda.common.theme.TextWhite
                )

                Text(
                    text = coins.priceChange1d.toString() + "%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (coins.priceChange1d < 0) com.mathroda.common.theme.CustomRed else com.mathroda.common.theme.CustomGreen
                )
            }
        }

    }

    Divider(color = com.mathroda.common.theme.LightGray)
}