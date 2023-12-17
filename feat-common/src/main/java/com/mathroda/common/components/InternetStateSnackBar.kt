package com.mathroda.common.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mathroda.common.R
import com.mathroda.common.theme.CustomGreen
import com.mathroda.common.theme.CustomRed
import com.mathroda.common.theme.TextWhite
import com.mathroda.core.state.InternetState
import kotlinx.coroutines.delay
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@ExperimentalAnimationApi
@Composable
fun InternetConnectivitySnackBar(
    internetState: InternetState,
    modifier: Modifier = Modifier
) {
    val state = rememberInternetSnackBarState()

    AnimatedVisibility(
        visible = state.isOpen,
        enter = fadeIn() + slideInVertically(),
        exit = slideOutVertically() + fadeOut(),
    ) {
        SnackBarBody(
            state = state,
            modifier = modifier.fillMaxWidth()
        )
    }

    LaunchedEffect(internetState) {
        when(internetState) {
            is InternetState.NotAvailable -> {
                state.showSnackBar(
                    color = CustomRed,
                    icon = R.drawable.ic_internet_offline,
                    message = "No Internet Connection!"
                )
            }

            is InternetState.Available -> {
                state.showSnackBar(
                    color = CustomGreen,
                    icon = R.drawable.ic_internet_online,
                    message = "Back Online!"
                )

                delay(3.toDuration(DurationUnit.SECONDS))

                state.close()
            }

            is InternetState.IDLE -> {
                if (state.isOpen) {
                    state.close()
                }
            }
        }
    }
}

@Composable
private fun SnackBarBody(
    state: InternetStateSnackBar,
    modifier: Modifier = Modifier
) {
   Surface(
       modifier = modifier,
       color = state.data.color
   ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp),
           horizontalArrangement = Arrangement.Center,
           verticalAlignment = Alignment.CenterVertically
       ) {
           state.data.icon?.let {
               Icon(
                   painter = painterResource(id = it),
                   contentDescription = "Internet State Icon",
                   tint = TextWhite
               )
           }

           Spacer(modifier = Modifier.width(8.dp))

           Text(
               text = state.data.message,
               color = TextWhite
           )
       }
   }
}

internal class InternetStateSnackBar {
    data class InternetStateData(
        val color: Color = Color.Transparent,
        @DrawableRes val icon: Int? = null,
        val message: String = ""
    )

    private val _isOpen = mutableStateOf(false)
    val isOpen by _isOpen

    private val _data = mutableStateOf(InternetStateData())
    val data by _data

    fun showSnackBar(
        color: Color,
        @DrawableRes icon: Int,
        message: String
    ) {
        _data.value = _data.value.copy(
            color = color,
            icon = icon,
            message = message
        )

        _isOpen.value = true
    }

    fun close() {
        _isOpen.value = false
    }
}

@Composable
internal fun rememberInternetSnackBarState() = remember {
    InternetStateSnackBar()
}