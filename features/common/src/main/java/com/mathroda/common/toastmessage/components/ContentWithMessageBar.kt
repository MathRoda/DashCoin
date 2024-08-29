package com.mathroda.common.toastmessage.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.mathroda.common.theme.CustomGreen
import com.mathroda.common.theme.CustomRed
import com.mathroda.common.theme.TextWhite

val LocalMessageBar = compositionLocalOf<MessageBarState> { error("error occurred while showing a custom bar") }

@Composable
fun rememberMessageBarState(): MessageBarState {
    return remember { MessageBarState() }
}

enum class MessageBarPosition {
    TOP,
    BOTTOM
}

/**
 * Composable function used to wrap the content around which you want to display a message bar.
 * @param messageBarState - Used to control the message bar itself.
 * @param position - Configure where you want to position the Message Bar.
 * [MessageBarPosition.TOP] and [MessageBarPosition.BOTTOM] as available.
 * @param visibilityDuration - How long the Message Bar should stay visible. A default value is 3 seconds.
 * @param showCopyButton - Whether to show a copy button, on a message bar of an error type.
 * @param copyButtonFontSize - The font size of the of the copy button text.
 * @param copyButtonFontStyle - [FontStyle] of the copy button text.
 * @param copyButtonFontWeight - [FontWeight] of the copy button text.
 * @param copyButtonFontFamily - [FontFamily] of the copy button text.
 * @param onMessageCopied - This lambda is used to display a custom info message, when a user clicks
 * the copy button and copy an error message to the clipboard. Usually a Toast message like 'Copied' is fine.
 * @param successIcon - Customize the [ImageVector] icon of a success message.
 * @param errorIcon - Customize the [ImageVector] icon of an error message.
 * @param errorMaxLines - In how many lines of text you want to display an error message.
 * @param successMaxLines - In how many lines of text you want to display a success message.
 * @param fontSize - The font size of the of the Success/Error text.
 * @param fontStyle - [FontStyle] of the Success/Error text.
 * @param fontWeight - [FontWeight] of the Success/Error text.
 * @param fontFamily - [FontFamily] of the Success/Error text.
 * @param contentBackgroundColor - The background color on top of which the [content] lambda will be placed.
 * @param successContainerColor - Container color of a success message bar.
 * @param successContentColor - Text color of the success message bar.
 * @param errorContainerColor - Container color of an error message bar.
 * @param errorContentColor - Text color of an error message bar.
 * @param enterAnimation - Enter animation of the message bar.
 * @param exitAnimation - Exit animation of the message bar.
 * @param verticalPadding - Vertical padding of the message bar.
 * @param horizontalPadding - Horizontal padding of the message bar.
 * @param content - Content composable around which you are displaying the message bar.
 * */
@Composable
fun ContentWithMessageBar(
    modifier: Modifier = Modifier,
    messageBarState: MessageBarState,
    position: MessageBarPosition = MessageBarPosition.TOP,
    showCopyButton: Boolean = true,
    copyButtonFontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
    copyButtonFontStyle: FontStyle = FontStyle.Normal,
    copyButtonFontWeight: FontWeight = FontWeight.Normal,
    copyButtonFontFamily: FontFamily? = null,
    onMessageCopied: (() -> Unit)? = null,
    successIcon: ImageVector = Icons.Default.Check,
    errorIcon: ImageVector = Icons.Default.Warning,
    errorMaxLines: Int = 1,
    successMaxLines: Int = 1,
    fontSize: TextUnit = MaterialTheme.typography.h2.fontSize,
    fontStyle: FontStyle = FontStyle.Normal,
    fontWeight: FontWeight = FontWeight.Normal,
    fontFamily: FontFamily? = null,
    contentBackgroundColor: Color = Color.Transparent,
    successContainerColor: Color = CustomGreen,
    successContentColor: Color = TextWhite,
    errorContainerColor: Color = CustomRed,
    errorContentColor: Color = TextWhite,
    enterAnimation: EnterTransition = expandVertically(
        animationSpec = tween(durationMillis = 300),
        expandFrom = if (position == MessageBarPosition.TOP)
            Alignment.Top else Alignment.Bottom
    ),
    exitAnimation: ExitTransition = shrinkVertically(
        animationSpec = tween(durationMillis = 300),
        shrinkTowards = if (position == MessageBarPosition.TOP)
            Alignment.Top else Alignment.Bottom
    ),
    verticalPadding: Dp = 12.dp,
    horizontalPadding: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = contentBackgroundColor)
    ) {
        content()
        MessageBarComponent(
            messageBarState = messageBarState,
            position = position,
            successIcon = successIcon,
            errorIcon = errorIcon,
            showCopyButton = showCopyButton,
            copyButtonFontSize = copyButtonFontSize,
            copyButtonFontStyle = copyButtonFontStyle,
            copyButtonFontWeight = copyButtonFontWeight,
            copyButtonFontFamily = copyButtonFontFamily,
            errorMaxLines = errorMaxLines,
            successMaxLines = successMaxLines,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            successContainerColor = successContainerColor,
            successContentColor = successContentColor,
            errorContainerColor = errorContainerColor,
            errorContentColor = errorContentColor,
            enterAnimation = enterAnimation,
            exitAnimation = exitAnimation,
            verticalPadding = verticalPadding,
            horizontalPadding = horizontalPadding,
            onMessageCopied = onMessageCopied
        )
    }
}

@Composable
internal fun MessageBarComponent(
    messageBarState: MessageBarState,
    position: MessageBarPosition,
    successIcon: ImageVector,
    errorIcon: ImageVector,
    errorMaxLines: Int,
    successMaxLines: Int,
    fontSize: TextUnit,
    fontStyle: FontStyle,
    fontWeight: FontWeight,
    fontFamily: FontFamily?,
    successContainerColor: Color,
    successContentColor: Color,
    errorContainerColor: Color,
    errorContentColor: Color,
    enterAnimation: EnterTransition,
    exitAnimation: ExitTransition,
    verticalPadding: Dp,
    horizontalPadding: Dp,
    showCopyButton: Boolean,
    copyButtonFontSize: TextUnit,
    copyButtonFontStyle: FontStyle,
    copyButtonFontWeight: FontWeight,
    copyButtonFontFamily: FontFamily?,
    onMessageCopied: (() -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var showMessageBar by remember { mutableStateOf(false) }
    val error by rememberUpdatedState(newValue = messageBarState.error?.message)
    val message by rememberUpdatedState(newValue = messageBarState.success)
    val timerManager = remember { TimerManager() }

    DisposableEffect(key1 = messageBarState.updated) {
        showMessageBar = true
        timerManager.scheduleTimer(
            scope = scope,
            visibilityDuration = messageBarState.duration,
            onTimerTriggered = {
                showMessageBar = false
            }
        )
        onDispose { timerManager.cancelTimer() }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = if (position == MessageBarPosition.TOP)
            Arrangement.Top else Arrangement.Bottom
    ) {
        AnimatedVisibility(
            visible = messageBarState.error != null && showMessageBar
                    || messageBarState.success != null && showMessageBar,
            enter = enterAnimation,
            exit = exitAnimation
        ) {
            MessageBar(
                message = message,
                error = error,
                successIcon = successIcon,
                errorIcon = errorIcon,
                errorMaxLines = errorMaxLines,
                successMaxLines = successMaxLines,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                successContainerColor = successContainerColor,
                successContentColor = successContentColor,
                errorContainerColor = errorContainerColor,
                errorContentColor = errorContentColor,
                verticalPadding = verticalPadding,
                horizontalPadding = horizontalPadding,
                showCopyButton = showCopyButton,
                copyButtonFontSize = copyButtonFontSize,
                copyButtonFontStyle = copyButtonFontStyle,
                copyButtonFontWeight = copyButtonFontWeight,
                copyButtonFontFamily = copyButtonFontFamily,
                onMessageCopied = onMessageCopied
            )
        }
    }
}

@Composable
internal fun MessageBar(
    message: String?,
    error: String?,
    successIcon: ImageVector,
    errorIcon: ImageVector,
    errorMaxLines: Int,
    successMaxLines: Int,
    fontSize: TextUnit,
    fontStyle: FontStyle,
    fontWeight: FontWeight,
    fontFamily: FontFamily?,
    successContainerColor: Color,
    successContentColor: Color,
    errorContainerColor: Color,
    errorContentColor: Color,
    verticalPadding: Dp,
    horizontalPadding: Dp,
    showCopyButton: Boolean,
    copyButtonFontSize: TextUnit,
    copyButtonFontStyle: FontStyle,
    copyButtonFontWeight: FontWeight,
    copyButtonFontFamily: FontFamily?,
    onMessageCopied: (() -> Unit)? = null
) {
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (error != null) errorContainerColor
                else successContainerColor
            )
            .padding(vertical = verticalPadding)
            .padding(horizontal = horizontalPadding)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(4f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector =
                if (error != null) errorIcon
                else successIcon,
                contentDescription = "Message Bar Icon",
                tint = if (error != null) errorContentColor
                else successContentColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message ?: (error ?: "Unknown"),
                color = if (error != null) errorContentColor
                else successContentColor,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (error != null) errorMaxLines else successMaxLines
            )
        }
        if (error != null && showCopyButton) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(text = "$error"))
                        onMessageCopied?.invoke()
                    },
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text(
                        text = "Copy",
                        color = errorContentColor,
                        fontSize = copyButtonFontSize,
                        fontStyle = copyButtonFontStyle,
                        fontWeight = copyButtonFontWeight,
                        fontFamily = copyButtonFontFamily,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}