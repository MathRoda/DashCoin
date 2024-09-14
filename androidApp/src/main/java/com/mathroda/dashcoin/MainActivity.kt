package com.mathroda.dashcoin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mathroda.shared.MainView
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.permissionUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalLayoutApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
       /* NotifierManager.initialize(
            NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_coin,
                notificationIconColorResId = R.color.orange,
                notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
                    id = "CHANNEL_ID_GENERAL",
                    name = "General"
                )
            )
        )
        val permissionUtil = NotifierManager.getPermissionUtil()
        permissionUtil.askNotificationPermission()*/
        setContent {
            MainView()
        }
    }
}


