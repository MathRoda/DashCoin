package com.mathroda.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mathroda.common.state.DialogState
import com.example.shared.CoinById


@Composable
fun DeleteAllCoinsDialog(
    dialogState: DialogState,
    onDialogStateChanged: (DialogState) -> Unit,
    onClick: () -> Unit
) {
    if (dialogState == DialogState.Open) {
        Dialog(onDismissRequest = { DialogState.Close }) {
            CustomDialogUI(
                title = "Are You Sure",
                description = "You want to delete all your favorite coins ?",
                onDialogStateChanged = onDialogStateChanged,
                onClick = { onClick() }
            )
        }
    }
}

@Composable
fun CustomDialog(
    dialogState: DialogState,
    onDialogStateChanged: (DialogState) -> Unit,
    coin: com.example.shared.CoinById?,
    onClick: () -> Unit
) {
    if (dialogState == DialogState.Open) {
        Dialog(onDismissRequest = { DialogState.Close }) {
            CustomDialogUI(
                title = "Are You Sure",
                description = "You want to Unwatch ${coin?.id} ?",
                onDialogStateChanged = onDialogStateChanged,
                onClick = { onClick() }
            )
        }
    }
}


@Composable
fun CustomDialogUI(
    modifier: Modifier = Modifier,
    onDialogStateChanged: (DialogState) -> Unit,
    onClick: () -> Unit,
    title: String,
    description: String,
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
                    text = title,
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
                    text = description,
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
                    onDialogStateChanged(DialogState.Close)
                }) {

                    Text(
                        "Dismiss",
                        fontWeight = FontWeight.Bold,
                        color = com.mathroda.common.theme.TextWhite.copy(ContentAlpha.disabled),
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
                TextButton(onClick = {
                    onDialogStateChanged(DialogState.Close)
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
