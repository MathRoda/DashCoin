package com.mathroda.coin_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mathroda.common.theme.Gold
import com.mathroda.common.theme.TextWhite

@Composable
fun TopBarCoinDetail(
    coinSymbol: String,
    icon: String,
    navController: NavController,
    isFavorite: Boolean,
    onCLick: (Boolean) -> Unit

) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .requiredHeight(40.dp)
    ) {

        Box(modifier = Modifier
            .weight(2f),
            contentAlignment = Alignment.CenterStart
        ) {
            BackStackButton(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                navController.popBackStack()
            }
        }
        Box(
            modifier = Modifier
                .weight(6f),
            contentAlignment = Alignment.Center){
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
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
                    color = TextWhite,
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(2f),
            contentAlignment = Alignment.CenterEnd){

            FavoriteButton(
                modifier = Modifier.padding(8.dp),
                isFavorite = isFavorite,
                onCLick = onCLick
            )
        }

        }
}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = TextWhite,
    isFavorite: Boolean,
    onCLick: (Boolean) -> Unit


) {
    IconToggleButton(
        checked = isFavorite ,
        onCheckedChange = onCLick
    ) {
        Icon(
            tint =  if (isFavorite) Gold else color ,
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Default.StarBorder,
            contentDescription = null
        )
    }

}

@Composable
fun BackStackButton(
    modifier: Modifier = Modifier,
    color: Color = TextWhite,
    onCLick: () -> Unit
) {
    IconButton(onClick = { onCLick() }) {
        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = Icons.Outlined.KeyboardArrowLeft,
            contentDescription = null
        )
    }


}