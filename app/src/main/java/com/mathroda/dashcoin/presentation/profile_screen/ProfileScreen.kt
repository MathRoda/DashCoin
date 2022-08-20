package com.mathroda.dashcoin.presentation.profile_screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mathroda.dashcoin.presentation.MainActivity
import com.mathroda.dashcoin.presentation.coins_screen.components.TopBar
import com.mathroda.dashcoin.presentation.dialog_screen.CustomDialogSignOut
import com.mathroda.dashcoin.presentation.profile_screen.viewmodel.ProfileViewModel
import com.mathroda.dashcoin.presentation.signin_screen.components.CustomLoginButton
import com.mathroda.dashcoin.presentation.ui.theme.CustomBrightRed
import com.mathroda.dashcoin.presentation.ui.theme.CustomRed
import com.talhafaki.composablesweettoast.util.SweetToastUtil

@ExperimentalMaterialApi
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    var openDialogCustom = remember{ mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            TopBar(title = "Profile")
            Spacer(modifier = Modifier.size(32.dp))
            CustomLoginButton(
                text = "LOGOUT",
                modifier = Modifier.fillMaxWidth(),
                color = listOf(CustomRed, CustomBrightRed)
            ) {
                openDialogCustom.value = true
            }
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