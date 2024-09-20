package com.mathroda.common.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import com.mathroda.core.util.currentTimeMillis

interface MultipleClicksHandler {
    fun processClick(event: () -> Unit)

    companion object
}

private class MultipleClicksHandlerImpl: MultipleClicksHandler {

    private val current: Long
     get() = currentTimeMillis()

    private var lastEventTime: Long = 0

    override fun processClick(event: () -> Unit) {
        if (current - lastEventTime >= 1000L) {
            event.invoke()
        }

        lastEventTime = current
    }
}

fun MultipleClicksHandler.Companion.get(): MultipleClicksHandler {
    return MultipleClicksHandlerImpl()
}

fun Modifier.singleClick(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "singleClick"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    val multipleClicksHandler = rememberMultipleClicksHandler()
    this.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { multipleClicksHandler.processClick { onClick.invoke() } },
        role = role,
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() }
    )
}

@Composable
fun rememberMultipleClicksHandler(): MultipleClicksHandler {
    return remember { MultipleClicksHandler.get() }
}
