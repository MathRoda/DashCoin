package com.mathroda.news_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.compose.ui.input.key.Key.Companion.M
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mathroda.common.theme.*
import com.mathroda.domain.NewsDetail
import com.mathroda.news_screen.R

@Composable
fun NewsCard(
    news: NewsDetail,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(15.dp),
                color = LighterGray
            )
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .weight(0.4f)
                .padding(6.dp)
                .requiredHeight(120.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = 0.dp,
            backgroundColor = DarkGray
        ) {
            AsyncImage(
                model = news.imgURL,
                contentDescription = news.description,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_news),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.6f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(news.relatedCoins) { coin ->
                    Text(
                        text = coin.toString(),
                        color = Gold,
                        style = Typography.body2,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.size(4.dp))

                }
            }

            Column(
                modifier = Modifier.padding(4.dp),
            ) {
                Text(
                    text = news.title,
                    color = TextWhite,
                    style = Typography.h4,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_news),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = news.source,
                    color = Twitter,
                    style = Typography.body2,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}