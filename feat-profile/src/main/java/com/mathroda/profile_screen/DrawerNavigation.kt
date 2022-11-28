package com.mathroda.profile_screen

import android.transition.Visibility
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.common.components.CustomDialogSignOut
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.navigation.Screens
import com.mathroda.common.theme.*
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DrawerNavigation(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    viewModel.uiState()
    val uriHandler = LocalUriHandler.current
    val userCredential = viewModel.userCredential.collectAsState()
    val isPremium = userCredential.value.isUserPremium()


    DrawerHeader(
        welcomeUser = userCredential.value.userName ?: "Hi DashCoiner",
        userEmail = userCredential.value.email,
        iconVisibility = isPremium
    )

    DrawerBody(
        item = listOf(
            com.mathroda.profile_screen.menuitem.MenuItems(
                id = "settings",
                title = "Settings",
                contentDescription = "Toggle Home",
                icon = Icons.Default.Settings
            ),
            com.mathroda.profile_screen.menuitem.MenuItems(
                id = "help",
                title = "Help Center",
                contentDescription = "Toggle About",
                icon = Icons.Default.Help
            ),
            com.mathroda.profile_screen.menuitem.MenuItems(
                id = "about",
                title = "About DashCoin",
                contentDescription = "Toggle About",
                icon = Icons.Default.Favorite
            )
        ),
        onItemClick = {
            when (it.id) {
                "about" -> {
                    uriHandler.openUri(Constants.DASHCOIN_REPOSITORY)
                }
            }
        }
    )

    /**
     * Logic for Login / Sign-out Buttons
     **/

    when(viewModel.authState.value) {
        is UserState.AuthedUser -> LogOut(navController)
        is UserState.UnauthedUser -> Login(navController)
        is UserState.PremiumUser -> LogOut(navController)
    }

    DrawerFooter()

}

@Composable
fun DrawerHeader(
    welcomeUser: String?,
    userEmail: String?,
    iconVisibility: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .padding(bottom = 16.dp)
            .background(com.mathroda.common.theme.BackgroundBlue)
    ) {

        Image(
            modifier = Modifier
                .graphicsLayer(scaleY = 0.7f, scaleX = 0.7f),
            painter = painterResource(id = R.drawable.profile_placeholder),
            contentDescription = "Profile Placeholder"
        )
        Row(horizontalArrangement = Arrangement.Center ) {
            AnimatedVisibility(visible = iconVisibility) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Premium User",
                    tint = Gold
                    )
            }
            Text(text = "Hi! $welcomeUser", fontSize = 19.sp)
        }

        Text(text = userEmail ?: "", fontSize = 17.sp)
    }

}

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

@Composable
fun Login(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        TopAppBar(title = { Text(text = "Profile") })
        Spacer(modifier = Modifier.size(32.dp))
        CustomLoginButton(
            text = "LOGIN",
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGray)
                .padding(top = 24.dp),
            color = listOf(
                CustomGreen,
                CustomBrightGreen
            )
        ) {
            navController.navigate(Screens.SignIn.route)
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun LogOut(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val openDialogCustom = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        TopAppBar(title = { Text(text = "Profile") })
        Spacer(modifier = Modifier.size(32.dp))
        CustomLoginButton(
            text = "LOGOUT",
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGray)
                .padding(top = 24.dp),
            color = listOf(
                CustomRed,
                CustomBrightRed
            )
        ) {
            openDialogCustom.value = true
        }
    }

    if (openDialogCustom.value) {
        CustomDialogSignOut(openDialogCustom = openDialogCustom) {
            viewModel.signOut()
            navController.popBackStack()
            navController.navigate(Screens.CoinsScreen.route)
        }
    }
}

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
            color = com.mathroda.common.theme.Gold
        )

        LazyRow {
            item {
                IconButton(onClick = { uriHandler.openUri(com.mathroda.core.util.Constants.LINKEDIN) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_linkedin),
                        modifier = Modifier.size(18.dp),
                        contentDescription = "Linkedin"
                    )
                }
                Spacer(modifier = Modifier.size(12.dp))

                IconButton(onClick = { uriHandler.openUri(com.mathroda.core.util.Constants.GITHUB) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_github),
                        modifier = Modifier.size(18.dp),
                        contentDescription = "Github"
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                IconButton(onClick = { uriHandler.openUri(com.mathroda.core.util.Constants.TWITTER) }) {
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
