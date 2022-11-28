package com.mathroda.coin_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mathroda.common.state.DialogState
import com.mathroda.common.theme.LightGray
import com.mathroda.common.theme.TextWhite

@Composable
fun NotPremiumDialog(
    dialogState: MutableState<DialogState>
) {
    if (dialogState.value == DialogState.Open) {
        Dialog(onDismissRequest = { DialogState.Close }) {
            NotPremiumUi(dialogState = dialogState)
        }
    }
}

@Composable
private fun NotPremiumUi(
    modifier: Modifier = Modifier,
    dialogState: MutableState<DialogState>
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = modifier.background(LightGray)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                IconButton(
                    onClick = { dialogState.value = DialogState.Close},
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = TextWhite,
                    )
                }

                Text(
                    text = "You are not premium yet",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.h1,
                    maxLines = 2,
                    color = TextWhite,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}