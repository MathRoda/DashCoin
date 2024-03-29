package com.mathroda.profile_screen.drawer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.common.components.CustomDialogSignOut
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.navigation.Destinations
import com.mathroda.common.theme.*
import com.mathroda.core.util.Constants
import com.mathroda.profile_screen.ProfileViewModel
import com.mathroda.profile_screen.menuitem.MenuItems

@Composable
fun DrawerBody(
    item: List<com.mathroda.profile_screen.menuitem.MenuItems>,
    modifier: Modifier = Modifier,
    onItemClick: (com.mathroda.profile_screen.menuitem.MenuItems) -> Unit
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
                    icon = Icons.Default.Help
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