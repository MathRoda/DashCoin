package com.mathroda.profile_screen.drawer

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun PickImageButton(
    modifier: Modifier = Modifier,
    onPickImage: (bitmap: Bitmap) -> Unit,
) {
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media
                    .getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }

            bitmap?.let { safeBitmap ->
                onPickImage(safeBitmap)
            }
        }
    }

    IconButton(
        modifier= modifier,
        onClick = {
            galleryLauncher.launch("image/*")
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
                .padding(6.dp)
        )
    }
}