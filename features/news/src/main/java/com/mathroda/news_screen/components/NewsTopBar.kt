package com.mathroda.news_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.TextWhite
import com.mathroda.news_screen.R

@Composable
fun NewsTopBar(
    title: String,
    isClicked: Boolean,
    onCLick: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {

        Box(
            modifier = Modifier.weight(0.7f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h2,
                color = TextWhite
            )
        }

        Spacer(modifier = Modifier.weight(0.2f))

        Box(
            modifier = Modifier.weight(0.1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            FilterButton(isClicked = isClicked) {
                onCLick(!isClicked)
            }
        }
    }
}

@Composable
private fun FilterButton(
    isClicked: Boolean,
    onCLick: () -> Unit
) {
    IconToggleButton(
        checked = isClicked,
        onCheckedChange = { onCLick() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = null,
            tint = TextWhite
        )
    }
}