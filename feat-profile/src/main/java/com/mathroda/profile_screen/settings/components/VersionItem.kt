package com.mathroda.profile_screen.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mathroda.profile_screen.util.getVersionName
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

@Composable
fun VersionItem() {

    val title = "App Version"
    val context = LocalContext.current
    val description = context.getVersionName()
    val uriHandler = LocalUriHandler.current
    var numberOfClicks by remember { mutableIntStateOf(0) }
    val onClick: () -> Unit = {
        if (++numberOfClicks == 7) {
            uriHandler.openUri(EasterEggUrl)
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { numberOfClicks }
            .filter { it > 0 }
            .collectLatest {
                delay(1_000)
                numberOfClicks = 0
            }
    }

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .height(48.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body2,
        )

        Text(
            text = description,
            style = MaterialTheme.typography.caption,
        )

    }
}

private const val EasterEggUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"