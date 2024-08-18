package com.mathroda.profile_screen.drawer

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants
import com.mathroda.profile_screen.ProfileViewModel
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import org.koin.androidx.compose.koinViewModel

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DrawerNavigation(
    viewModel: ProfileViewModel,
    navigateToSettings: () -> Unit,
    navigateToSignIn: () -> Unit,
    closeDrawer: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val userCredential = viewModel.userCredential.collectAsState()
    val isPremium by viewModel.isUserPremium.collectAsState()
    val authState by viewModel.authState.collectAsState()
    val isAuthedUser = authState !is UserState.UnauthedUser
    val updateProfilePictureState = viewModel.updateProfilePictureState.collectAsState()
    val menuItems = viewModel.getMenuListItems()
    val toast = viewModel.toastState.value

    if (toast.first) {
        SweetToastUtil.SweetSuccess(
            message = toast.second,
            padding = PaddingValues(bottom = 65.dp)
        )
        viewModel.updateToastState(false, "")
    }

    LaunchedEffect(Unit) {
        viewModel.init()
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
        item = menuItems,
        onItemClick = {
            when (it.id) {
                "about" -> uriHandler.openUri(Constants.DASHCOIN_REPOSITORY)
                "settings" -> navigateToSettings()
                "syncData" -> viewModel.onSyncClicked()
            }
        }
    )

    DrawerFooter(
        signOut = viewModel::signOut,
        userState = authState,
        navigateToSignIn = navigateToSignIn,
        closeDrawer = {
            closeDrawer()
        }
    )

}