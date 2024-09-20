@file:Suppress("DEPRECATION")

package com.mathroda.profile.drawer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher

@Composable
fun PickImageButton(
    modifier: Modifier = Modifier,
    onPickImage: (bitmap: ByteArray) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val imagerPicker = rememberImagePickerLauncher(
        scope = scope,
        selectionMode = SelectionMode.Single,
        onResult = { byteArrays ->
            byteArrays
                .firstOrNull()
                ?.let(onPickImage)
        }
    )

    IconButton(
        modifier= modifier,
        onClick = {
            imagerPicker.launch()
        }
    ) {
        Icon(
            imageVector = Icons.Filled.CameraAlt,
            contentDescription = "Pick image",
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.background.copy(alpha = 0.8f),
                    shape = CircleShape
                )
                .padding(10.dp)
        )
    }
}