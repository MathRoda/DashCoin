package com.mathroda.coins.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.mathroda.common.resources.Res
import com.mathroda.common.resources.ic_filter
import com.mathroda.common.resources.ic_search
import com.mathroda.common.theme.LighterGray
import com.mathroda.common.theme.TextWhite
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SearchBar(
    hint: String,
    modifier: Modifier = Modifier,
    state: MutableState<TextFieldValue>

) {
    val focusRequester = remember { FocusRequester() }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(35.dp))
            .background(LighterGray)
            .padding(5.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_filter),
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier
                .size(35.dp)
                .padding(start = 12.dp),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {


            BasicTextField(
                value = state.value,
                onValueChange = {
                    state.value = it
                },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = TextWhite),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isHintDisplayed = !it.isFocused
                    }
                    .focusRequester(focusRequester),
                cursorBrush = SolidColor(TextWhite)
            )

            if (isHintDisplayed) {
                Text(
                    text = hint,
                    color = TextWhite,
                    modifier = Modifier
                )
            }

        }
    }
}


