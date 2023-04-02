package com.mathroda.favorite_coins.components.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mathroda.common.util.getColorStatus

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun WatchlistItem(
    modifier: Modifier = Modifier,
    icon: String,
    coinName: String,
    symbol: String,
    rank: String,
    marketStatus: Double = 0.0,
    onItemClick: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .padding(10.dp, 5.dp, 10.dp, 10.dp),
        elevation = 12.dp,
        backgroundColor = com.mathroda.common.theme.LighterGray,
        onClick = onItemClick

    ) {
        Column(
            Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, marketStatus.getColorStatus()),

                        ),
                    shape = RoundedCornerShape(10.dp),
                    alpha = 0.4f
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .weight(7f)
                ) {
                    AsyncImage(
                        model = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp),
                    )
                }


                Column(
                    modifier = Modifier
                        .weight(3f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Statics",
                            color = com.mathroda.common.theme.TextWhite,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            tint = com.mathroda.common.theme.TextWhite,
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = 0.8f
                                    scaleY = 0.8f
                                }
                                .padding(start = 5.dp),
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }

                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .weight(8f)
                ) {
                    Text(
                        text = coinName,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(3f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .background(com.mathroda.common.theme.LightGray)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center

                        ) {
                            Text(
                                text = rank,
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.SemiBold,
                                color = com.mathroda.common.theme.Gold,
                            )
                        }
                        Text(
                            text = symbol,
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold,
                            color = com.mathroda.common.theme.TextWhite,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.Bottom)
                        )
                    }

                }

            }

        }


    }
}