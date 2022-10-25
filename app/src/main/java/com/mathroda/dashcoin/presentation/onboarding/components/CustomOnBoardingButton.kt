package com.mathroda.dashcoin.presentation.onboarding.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.mathroda.dashcoin.presentation.onboarding.utils.OnBoardingPage
import com.mathroda.dashcoin.presentation.ui.theme.CustomGreen
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CustomOnBoardingButton(
    text: String = "Continue Now",
    modifier: Modifier = Modifier,
    color: Color = CustomGreen,
    fontWeight: FontWeight = FontWeight.SemiBold,
    pagerState: PagerState,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 64.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = pagerState.currentPage == 2,
            modifier.fillMaxWidth()
            ) {

            Button(
                onClick = { onClick() },
                modifier = modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                ),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = fontWeight
                )
            }
        }
    }

}
