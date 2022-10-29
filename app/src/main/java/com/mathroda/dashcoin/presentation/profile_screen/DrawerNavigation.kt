package com.mathroda.dashcoin.presentation.profile_screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mathroda.dashcoin.R
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.navigation.main.Screens
import com.mathroda.dashcoin.navigation.root.Graph
import com.mathroda.dashcoin.presentation.MainActivity
import com.mathroda.dashcoin.presentation.dialog_screen.CustomDialogSignOut
import com.mathroda.dashcoin.presentation.profile_screen.menuitem.MenuItems
import com.mathroda.dashcoin.presentation.profile_screen.viewmodel.ProfileViewModel
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomLoginButton
import com.mathroda.dashcoin.presentation.ui.theme.*
import javax.security.auth.login.LoginException

@ExperimentalMaterialApi
@Composable
fun DrawerNavigation(
    welcomeUser: String,
    isUserExists: Boolean,
    navController: NavController
) {
    val uriHandler  = LocalUriHandler.current

    DrawerHeader(welcomeUser = welcomeUser)

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
        ) ,
        onItemClick = {
            when(it.id) {
                "about" -> {uriHandler.openUri(Constants.DASHCOIN_REPOSITORY)}
            }
        }
    )

    /**
     * Logic for Login / Signout Buttons
     **/

    if (isUserExists) LogOut() else Login(navController)

    DrawerFooter()

}

@Composable
fun DrawerHeader(
    welcomeUser: String
) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier.graphicsLayer(scaleY = 0.5f, scaleX = 0.5f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_placeholder),
                    contentDescription = "Profile Placeholder"
                )
            }
            Text(text = welcomeUser, fontSize = 22.sp)
        }

}

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
            color = listOf(CustomGreen, CustomBrightGreen)
        ) {
            navController.navigate(Screens.SignIn.route)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun LogOut(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val activity = (LocalContext.current as? Activity)
    val openDialogCustom = remember{ mutableStateOf(false) }
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
                color = listOf(CustomRed, CustomBrightRed)
            ) {
                openDialogCustom.value = true
            }
    }

    if (openDialogCustom.value) {
        CustomDialogSignOut(openDialogCustom = openDialogCustom) {
            viewModel.signOut()
            activity?.finish()
            activity?.startActivity(Intent(activity, MainActivity::class.java))
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