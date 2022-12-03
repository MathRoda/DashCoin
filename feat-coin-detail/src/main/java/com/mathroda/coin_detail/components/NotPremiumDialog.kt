package com.mathroda.coin_detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.*
import com.mathroda.common.state.DialogState
import com.mathroda.common.theme.CustomGreen
import com.mathroda.common.theme.Gold
import com.mathroda.common.theme.LightGray
import com.mathroda.common.theme.TextWhite
import com.mathroda.core.util.Constants.UPGRADE_TO_PREMIUM

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

@Composable
private fun NotPremiumUi(
    modifier: Modifier = Modifier,
    dialogState: MutableState<DialogState>
) {
    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.mathroda.coin_detail.R.raw.premium))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever,
    )
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(Gold.copy(alpha = 0.8f), LightGray),
                ),
            )
        ) {

            Column {
                IconButton(
                    onClick = { dialogState.value = DialogState.Close },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = TextWhite,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .height(200.dp)
            ) {
                LottieAnimation(
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit,
                    composition = lottieComp,
                    progress = { lottieProgress },
                    alignment = Alignment.Center
                )
                Text(
                    text = UPGRADE_TO_PREMIUM,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 3,
                    color = TextWhite,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {  /* dialogState.value = DialogState.Close */ },
                    colors = ButtonDefaults.textButtonColors(backgroundColor = CustomGreen),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        "Upgrade",
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}