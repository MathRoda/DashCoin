package com.mathroda.coin_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import com.mathroda.common.components.NotPremiumUi
import com.mathroda.core.state.DialogState

@Composable
fun NotPremiumDialog(
    dialogState: MutableState<DialogState>
) {
    val visible = dialogState.value == DialogState.Open
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandIn(TweenSpec(700)),
        exit = ExitTransition.None
    ) {
        Dialog(onDismissRequest = { DialogState.Close }) {
            NotPremiumUi(dialogState = dialogState)
        }
    }
}