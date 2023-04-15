package com.mathroda.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mathroda.common.state.DialogState
import com.mathroda.domain.model.CoinById

@Composable
fun CustomDialog(
    dialogState: MutableState<DialogState>,
    coin: CoinById?,
    onClick: () -> Unit
) {
    if (dialogState.value == DialogState.Open) {
        Dialog(onDismissRequest = { DialogState.Close }) {
            CustomDialogUI(
                dialogState = dialogState,
                coin = coin,
                onClick = { onClick() }
            )
        }
    }

}


@Composable
fun CustomDialogUI(
    modifier: Modifier = Modifier,
    dialogState: MutableState<DialogState>,
    onClick: () -> Unit,
    coin: CoinById?,
) {

    Card(
        //shape = MaterialTheme.shapes.medium,
        shape = RoundedCornerShape(10.dp),
        // modifier = modifier.size(280.dp, 240.dp)
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier
                .background(com.mathroda.common.theme.LightGray)
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Are You Sure",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.h1,
                    maxLines = 2,
                    color = com.mathroda.common.theme.TextWhite,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "You want to Unwatch ${coin?.id} ?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.body1,
                    color = com.mathroda.common.theme.TextWhite

                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(com.mathroda.common.theme.LighterGray),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                TextButton(onClick = {
                    dialogState.value = DialogState.Close
                }) {

                    Text(
                        "Dismiss",
                        fontWeight = FontWeight.Bold,
                        color = com.mathroda.common.theme.TextWhite.copy(ContentAlpha.disabled),
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
                TextButton(onClick = {
                    dialogState.value = DialogState.Close
                    onClick()

                }) {
                    Text(
                        "Yes",
                        fontWeight = FontWeight.ExtraBold,
                        color = com.mathroda.common.theme.TextWhite,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}
