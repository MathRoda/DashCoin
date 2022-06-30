package com.mathroda.dashcoin.presentation.coin_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray
import com.mathroda.dashcoin.presentation.ui.theme.Twitter

@Composable
fun LinksSection(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier)
    {
        LinkButton(
            modifier = Modifier
                .padding(start = 20.dp, bottom = 20.dp, top = 20.dp)
                .clickable {
                }
                .clip(RoundedCornerShape(35.dp))
                .background(Twitter)
                .height(45.dp)
                .weight(1f),
            title = "Twitter"
        )
        LinkButton(
            modifier = Modifier
                .padding(start = 20.dp, bottom = 20.dp, top = 20.dp)
                .clickable {
                }
                .clip(RoundedCornerShape(35.dp))
                .background(LighterGray)
                .height(45.dp)
                .weight(1f),
            title = "Website"
        )
    }
}

@Composable
fun LinkButton(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            color = Color.White
        )
    }
}