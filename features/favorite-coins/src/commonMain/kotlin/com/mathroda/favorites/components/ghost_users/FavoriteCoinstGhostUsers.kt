@file:OptIn(ExperimentalResourceApi::class)

package com.mathroda.favorites.components.ghost_users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mathroda.common.components.CommonTopBar
import com.mathroda.common.components.CustomLoginButton
import com.mathroda.common.components.FavoriteCoinsAnimation
import com.mathroda.common.theme.DarkGray
import com.mathroda.common.theme.TextWhite
import com.mathroda.common.theme.Typography
import org.jetbrains.compose.resources.ExperimentalResourceApi

@ExperimentalResourceApi
@ExperimentalLayoutApi
@Composable
fun WatchListGhostUsers(
    navigateToSignIn: () -> Unit
) {
    Scaffold(
        topBar = { CommonTopBar(title = "Favorite Coins") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(DarkGray)
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FavoriteCoinsAnimation(
                modifier = Modifier
                    .padding(top = 16.dp, end = 24.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(CenterHorizontally),
                    text = "Feature limited for registered users",
                    style = Typography.body1,
                    color = TextWhite.copy(alpha = 0.6f)
                )
                CustomLoginButton(
                    text = "LOGIN",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkGray)
                        .padding(bottom = 24.dp),
                ) {
                    navigateToSignIn()
                }
            }
        }
    }
}