package com.mathroda.dashcoin.presentation.profile_screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.mathroda.dashcoin.R
import com.mathroda.dashcoin.core.util.Constants
import com.mathroda.dashcoin.presentation.MainActivity
import com.mathroda.dashcoin.presentation.coins_screen.components.CoinsScreenTopBar
import com.mathroda.dashcoin.presentation.dialog_screen.CustomDialogSignOut
import com.mathroda.dashcoin.presentation.profile_screen.menuitem.MenuItems
import com.mathroda.dashcoin.presentation.profile_screen.viewmodel.ProfileViewModel
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomLoginButton
import com.mathroda.dashcoin.presentation.ui.theme.CustomBrightRed
import com.mathroda.dashcoin.presentation.ui.theme.CustomRed

@ExperimentalMaterialApi
@Composable
fun DrawerNavigation(
    welcomeUser: String
) {
    DrawerHeader(welcomeUser =welcomeUser)

    DrawerBody(
        item = listOf(
            MenuItems(
                id = "home",
                title = "HOME",
                contentDescription = "Toggle Home",
                icon = Icons.Default.Home
            ),
            MenuItems(
                id = "about",
                title = "ABOUT",
                contentDescription = "Toggle About",
                icon = Icons.Default.Help
            ),
        ) ,
        onItemClick = {}
    )

    LogOut()

}

@Composable
fun DrawerHeader(
    welcomeUser: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center,
    ) {
         //Text(text = "Welcome", fontSize = 28.sp)
         Text(text = welcomeUser, fontSize = 26.sp)
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

@ExperimentalMaterialApi
@Composable
fun LogOut(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val activity = (LocalContext.current as? Activity)
    var openDialogCustom = remember{ mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

            //TopAppBar(title = { Text(text = "Profile") })
            Spacer(modifier = Modifier.size(32.dp))
            CustomLoginButton(
                text = "LOGOUT",
                modifier = Modifier.fillMaxWidth(),
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