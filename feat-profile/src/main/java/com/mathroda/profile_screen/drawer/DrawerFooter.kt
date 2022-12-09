package com.mathroda.profile_screen.drawer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.DashCoinTheme
import com.mathroda.common.theme.Gold
import com.mathroda.core.util.Constants
import com.mathroda.profile_screen.R

@Composable
fun DrawerFooter() {
    val uriHandler = LocalUriHandler.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Contact Creator",
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            color = Gold
        )

        LazyRow {
            item {
                IconButton(onClick = { uriHandler.openUri(Constants.LINKEDIN) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_linkedin),
                        modifier = Modifier.size(18.dp),
                        contentDescription = "Linkedin"
                    )
                }
                Spacer(modifier = Modifier.size(12.dp))

                IconButton(onClick = { uriHandler.openUri(Constants.GITHUB) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_github),
                        modifier = Modifier.size(18.dp),
                        contentDescription = "Github"
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                IconButton(onClick = { uriHandler.openUri(Constants.TWITTER) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_twitter),
                        modifier = Modifier.size(18.dp),
                        contentDescription = "Twitter"
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun DrawerFooterPreview() {
    DashCoinTheme {
        DrawerFooter()
    }
}