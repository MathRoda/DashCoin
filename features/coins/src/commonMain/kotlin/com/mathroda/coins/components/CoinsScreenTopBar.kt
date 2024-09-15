package com.mathroda.coins.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.TextWhite

@Composable
fun CoinsScreenTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigationDrawerClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {

        IconButton(onClick = { onNavigationDrawerClick() }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Toggle Drawer",
            )
        }

        Box(Modifier.fillMaxWidth()) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h2,
                color = TextWhite,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 32.dp)
            )
        }

    }

}