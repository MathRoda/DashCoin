package com.mathroda.profile_screen.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathroda.common.theme.DashCoinTheme
import com.mathroda.profile_screen.menuitem.MenuItems

@Composable
fun DrawerBody(
    item: List<MenuItems>,
    modifier: Modifier = Modifier,
    onItemClick: (MenuItems) -> Unit
) {

    LazyColumn(modifier = modifier) {
        items(item) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
            ) {

                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = TextStyle(fontSize = 18.sp),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

}

@Preview
@Composable
fun DrawerBodyPreview() {
    DashCoinTheme {
        DrawerBody(
            item = listOf(
                MenuItems(
                    id = "settings",
                    title = "Settings",
                    contentDescription = "Toggle Home",
                    icon = Icons.Default.Settings
                ),
                MenuItems(
                    id = "help",
                    title = "Help Center",
                    contentDescription = "Toggle About",
                    icon = Icons.AutoMirrored.Filled.Help
                ),
                MenuItems(
                    id = "about",
                    title = "About DashCoin",
                    contentDescription = "Toggle About",
                    icon = Icons.Default.Favorite
                )
            ),
            onItemClick = {}
        )
    }
}