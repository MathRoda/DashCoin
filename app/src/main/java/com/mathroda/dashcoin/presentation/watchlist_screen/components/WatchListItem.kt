package com.mathroda.dashcoin.presentation.watchlist_screen.components

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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mathroda.dashcoin.presentation.ui.theme.Gold
import com.mathroda.dashcoin.presentation.ui.theme.LightGray
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray
import com.mathroda.dashcoin.presentation.ui.theme.TextWhite

@ExperimentalMaterialApi
@Composable
fun WatchlistItem(
    icon: String,
    coinName: String,
    symbol: String,
    rank: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(10.dp, 5.dp, 10.dp, 10.dp),
        elevation = 8.dp,
        backgroundColor = LighterGray,
        onClick = onClick
    ) {
        Column {
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
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            tint = TextWhite,
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
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(LightGray)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center

                        ) {
                            Text(
                                text = rank,
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.Bold,
                                color = Gold,
                                modifier = Modifier
                            )
                        }
                        Text(
                            text = symbol,
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
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