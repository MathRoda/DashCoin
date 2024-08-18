package com.mathroda.profile_screen.drawer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathroda.common.components.CustomDialogSignOut
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.theme.CustomRed
import com.mathroda.common.theme.DarkGray
import com.mathroda.common.theme.Gold
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Constants
import com.mathroda.profile_screen.ProfileViewModel
import com.mathroda.profile_screen.R
import org.koin.androidx.compose.koinViewModel

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun DrawerFooter(
    userState: UserState,
    closeDrawer: () -> Unit,
    navigateToSignIn: () -> Unit,
    signOut: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)
    ) {

        when(userState) {
            is UserState.UnauthedUser -> Login(navigateToSignIn)
            else-> LogOut(
                closeDrawer = closeDrawer,
                signOut = signOut
            )
        }

        Spacer(modifier = Modifier.height(48.dp) )
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


@Composable
fun Login(
    navigateToSignIn: () -> Unit
) {
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
        ) {
            navigateToSignIn()
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun LogOut(
    signOut: () -> Unit,
    closeDrawer: () -> Unit,
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
            backgroundColor = CustomRed
        ) {
            openDialogCustom.value = true
        }
    }

    if (openDialogCustom.value) {
        CustomDialogSignOut(openDialogCustom = openDialogCustom) {
            signOut()
            closeDrawer()
        }
    }
}