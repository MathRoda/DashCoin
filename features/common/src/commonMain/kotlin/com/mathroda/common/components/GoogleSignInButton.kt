package com.mathroda.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathroda.common.theme.LighterGray
import `dash coin`.features.common.generated.resources.Res
import org.jetbrains.compose.resources.vectorResource

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .padding(bottom = 48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = LighterGray
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
        onClick = onClick
    ) {
        Image(
            imageVector = vectorResource(resource = Res.drawable.ic_google),
            contentDescription = null
        )
        Text(
            text = "Sign in with Google",
            modifier = Modifier.padding(6.dp),
            fontSize = 16.sp
        )
    }
}