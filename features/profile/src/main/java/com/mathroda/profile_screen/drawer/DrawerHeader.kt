package com.mathroda.profile_screen.drawer

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.mathroda.common.theme.BackgroundBlue
import com.mathroda.common.theme.DashCoinTheme
import com.mathroda.common.theme.Gold
import com.mathroda.profile_screen.R
import com.mathroda.profile_screen.drawer.components.PickImageButton
import com.mathroda.profile_screen.drawer.state.UpdatePictureState
import com.talhafaki.composablesweettoast.util.SweetToastUtil

@Composable
fun DrawerHeader(
    welcomeUser: String?,
    userEmail: String?,
    userImage: String?,
    iconVisibility: Boolean,
    isUserAuthed: Boolean,
    updateProfilePictureState: UpdatePictureState,
    clearUpdateProfilePictureState: () -> Unit,
    updateProfilePicture: (bitmap: Bitmap) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .padding(bottom = 16.dp)
            .background(BackgroundBlue)
    ) {

        Spacer(modifier = Modifier.weight(1f))

        ProfilePictureBox(
            modifier = Modifier
                .padding(bottom = 10.dp)
            ,
            userImage = userImage,
            isUserAuthed = isUserAuthed,
            updateProfilePictureState = updateProfilePictureState,
            clearUpdateProfilePictureState = clearUpdateProfilePictureState,
            updateProfilePicture = updateProfilePicture
        )

        Row(horizontalArrangement = Arrangement.Center ) {
            AnimatedVisibility(visible = iconVisibility) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Premium User",
                    tint = Gold
                )
            }
            Text(text = "Hi! $welcomeUser", fontSize = 19.sp)
        }

        userEmail?.let { userEmail ->
            Text(text = userEmail, fontSize = 17.sp)
        }

        Spacer(modifier = Modifier.weight(1f))
    }

}

@Composable
fun ProfilePictureBox(
    modifier: Modifier = Modifier,
    userImage: String?,
    isUserAuthed: Boolean,
    updateProfilePictureState: UpdatePictureState,
    clearUpdateProfilePictureState: () -> Unit,
    updateProfilePicture: (bitmap: Bitmap) -> Unit,
) {

    /**
     * Show update profile picture state toasts
     */

    if (updateProfilePictureState.isFailure) {
        SweetToastUtil.SweetError(
            message = "Something goes wrong! Try again later",
            padding = PaddingValues(bottom = 24.dp)
        )
        clearUpdateProfilePictureState()
    }

    if (updateProfilePictureState.isSuccess) {
        SweetToastUtil.SweetSuccess(
            message = "Profile picture updated successfully",
            padding = PaddingValues(bottom = 24.dp)
        )
        clearUpdateProfilePictureState()
    }

    var isAsyncImageSate by remember {
        mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty)
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = modifier
            .fillMaxWidth(fraction = 0.4f)
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = userImage,
            contentDescription = "Profile picture",
            onLoading = { isAsyncImageSate = it },
            onSuccess = { isAsyncImageSate = it },
            onError = { isAsyncImageSate = it },
            error = painterResource(id = R.drawable.profile_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )

        // Show progress indicator if -> picture is uploading || async image is loading
        if (
            updateProfilePictureState.isLoading
            || isAsyncImageSate is AsyncImagePainter.State.Loading
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colors.background.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onBackground
                )
            }
        }

        if (isUserAuthed) {
            PickImageButton(
                onPickImage = updateProfilePicture,
            )
        }
    }
}

@Preview(device = Devices.PIXEL_4)
@Composable
fun DrawerHeaderPreview() {
    DashCoinTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            DrawerHeader(
                welcomeUser = "John Doe",
                userEmail = "johndoe@gmail.com",
                userImage = null,
                iconVisibility = true,
                isUserAuthed = true,
                updateProfilePictureState = UpdatePictureState(),
                clearUpdateProfilePictureState = {},
                updateProfilePicture = {}
            )
        }
    }
}