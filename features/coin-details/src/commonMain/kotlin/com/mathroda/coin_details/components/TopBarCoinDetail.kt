package com.mathroda.coin_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathroda.common.components.BackStackButton
import com.mathroda.common.components.CommonAsyncImage
import com.mathroda.common.theme.Gold
import com.mathroda.common.theme.TextWhite
import com.mathroda.core.state.IsFavoriteState

@Composable
fun TopBarCoinDetail(
    coinSymbol: String,
    icon: String,
    isFavorite: IsFavoriteState,
    onCLick: (Boolean) -> Unit,
    popBackStack: () -> Unit

) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .requiredHeight(40.dp)
    ) {

        Box(
            modifier = Modifier
                .weight(2f),
            contentAlignment = Alignment.CenterStart
        ) {
            BackStackButton(
                modifier = Modifier
                    .padding(8.dp)
            ) {
               popBackStack()
            }
        }
        Box(
            modifier = Modifier
                .weight(6f),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                CommonAsyncImage(
                    model = icon,
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
            contentAlignment = Alignment.CenterEnd
        ) {

            FavoriteButton(
                modifier = Modifier.padding(8.dp),
                isFavorite = isFavorite,
                onCLick = onCLick
            )
        }

    }
}

@Composable
private fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: IsFavoriteState,
    onCLick: (Boolean) -> Unit


) {
    IconToggleButton(
        checked = isFavorite == IsFavoriteState.Favorite,
        onCheckedChange = onCLick
    ) {
        Icon(
            tint = when (isFavorite) {
                is IsFavoriteState.Favorite -> Gold
                is IsFavoriteState.NotFavorite -> TextWhite
            },
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = when (isFavorite) {
                is IsFavoriteState.Favorite -> Icons.Filled.Star
                is IsFavoriteState.NotFavorite -> Icons.Default.StarBorder
            },
            contentDescription = null
        )
    }

}
