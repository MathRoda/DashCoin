package com.mathroda.profile_screen.drawer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.common.components.CustomDialogSignOut
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.navigation.Destinations
import com.mathroda.common.theme.*
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants
import com.mathroda.profile_screen.ProfileViewModel
import com.mathroda.profile_screen.menuitem.MenuItems

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
    val isAuthedUser = viewModel.authState.value !is UserState.UnauthedUser
    val updateProfilePictureState = viewModel.updateProfilePictureState.collectAsState()

    DrawerHeader(
        welcomeUser = userCredential.value.userName ?: "Hi DashCoiner",
        userEmail = userCredential.value.email,
        userImage = userCredential.value.image,
        iconVisibility = isPremium,
        isUserAuthed = isAuthedUser,
        updatePictureState = updateProfilePictureState.value,
        clearUpdatePictureState = { viewModel.clearUpdateProfilePictureState() },
        updateProfilePicture = { bitmap ->
            viewModel.updateProfilePicture(bitmap = bitmap)
        }
    )

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
            navController.navigate(Destinations.SignIn.route)
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
            navController.navigate(Destinations.CoinsScreen.route)
        }
    }
}