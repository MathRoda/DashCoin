package com.mathroda.dashcoin.presentation.news_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mathroda.dashcoin.presentation.ui.theme.DarkGray
import com.mathroda.dashcoin.presentation.ui.theme.TextWhite

@ExperimentalMaterialApi
@Composable
fun NewsCard(
    newsThumb: String,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
        onClick = onClick,
        backgroundColor = DarkGray
    ) {
        Box(modifier = Modifier.height(200.dp))  {
            AsyncImage(
                model = newsThumb,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 150f
                    )
                )
            )
            
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.BottomStart
                ) {
                Text(
                    text = title,
                    style = TextStyle(color = TextWhite, fontSize = 16.sp)
                    )
            }
        }
    }
}