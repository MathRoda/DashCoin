@file:OptIn(ExperimentalResourceApi::class)

package com.mathroda.common.components

import KottieAnimation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mathroda.common.resources.Res
import com.mathroda.common.theme.CustomGreen
import com.mathroda.common.theme.Gold
import com.mathroda.common.theme.LightGray
import com.mathroda.common.theme.TextWhite
import com.mathroda.core.state.DialogState
import com.mathroda.core.util.Constants.UPGRADE_TO_PREMIUM
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import utils.KottieConstants

@Composable
fun NotPremiumUi(
    modifier: Modifier = Modifier,
    dialogState: MutableState<DialogState>
) {
    var animation by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        animation = Res.readBytes("files/premium.json").decodeToString()
    }

    val lottieComp =  rememberKottieComposition(
        spec = KottieCompositionSpec.File(animation)
    )
    val lottieProgress by animateKottieCompositionAsState(
        composition = lottieComp,
        iterations = KottieConstants.IterateForever,
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    KottieAnimation(
                        modifier = Modifier.fillMaxWidth(),
                        composition = lottieComp,
                        progress = { lottieProgress.progress },
                    )
                }

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