package com.mathroda.profile_screen.drawer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.common.navigation.Destinations
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
    val uriHandler = LocalUriHandler.current
    val userCredential = viewModel.userCredential.collectAsState()
    val isPremium by viewModel.isUserPremium().collectAsState(initial = false)
    val isAuthedUser = viewModel.authState.value !is UserState.UnauthedUser
    val updateProfilePictureState = viewModel.updateProfilePictureState.collectAsState()

    LaunchedEffect(true) {
        viewModel.updateUiState()
        viewModel.getUserCredential()
    }

    DrawerHeader(
        welcomeUser = userCredential.value.userName ?: "Hi DashCoiner",
        userEmail = userCredential.value.email,
        userImage = userCredential.value.image,
        iconVisibility = isPremium,
        isUserAuthed = isAuthedUser,
        updateProfilePictureState = updateProfilePictureState.value,
        clearUpdateProfilePictureState = { viewModel.clearUpdateProfilePictureState() },
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
          /*MenuItems(
                id = "help",
                title = "Help Center",
                contentDescription = "Toggle About",
                icon = Icons.Default.Help
            ),*/
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
                "settings" -> {
                    navController.navigate(Destinations.Settings.route)
                }
            }
        }
    )

    DrawerFooter(
        userState = viewModel.authState.value,
        navController = navController
    )

}