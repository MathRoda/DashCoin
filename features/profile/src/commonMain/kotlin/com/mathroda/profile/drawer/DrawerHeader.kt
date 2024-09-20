package com.mathroda.profile.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathroda.common.components.CommonAsyncImage
import com.mathroda.common.resources.Res
import com.mathroda.common.resources.profile_placeholder
import com.mathroda.common.theme.BackgroundBlue
import com.mathroda.common.theme.Gold
import com.mathroda.common.toastmessage.components.LocalMessageBar
import com.mathroda.profile.drawer.components.PickImageButton
import com.mathroda.profile.drawer.state.UpdatePictureState
import org.jetbrains.compose.resources.painterResource

@Composable
fun DrawerHeader(
    welcomeUser: String,
    userEmail: String?,
    userImage: String?,
    iconVisibility: Boolean,
    isUserAuthed: Boolean,
    updateProfilePictureState: UpdatePictureState,
    clearUpdateProfilePictureState: () -> Unit,
    updateProfilePicture: (bitmap: ByteArray) -> Unit,
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
            Text(text = welcomeUser, fontSize = 19.sp)
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
    updateProfilePicture: (bitmap: ByteArray) -> Unit,
) {
    val messageBarState = LocalMessageBar.current
    /**
     * Show update profile picture state toasts
     */

    if (updateProfilePictureState.isFailure) {
        messageBarState.showError(
            message = "Something goes wrong! Try again later",
        )
        clearUpdateProfilePictureState()
    }

    if (updateProfilePictureState.isSuccess) {
        messageBarState.showSuccess(
            message = "Profile picture updated successfully",
        )
        clearUpdateProfilePictureState()
    }


    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = modifier
            .fillMaxWidth(fraction = 0.4f)
            .aspectRatio(1f)
    ) {
        CommonAsyncImage(
            model = userImage,
            contentDescription = "Profile picture",
            contentScale = ContentScale.Crop,
            placeHolder = {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    painter = painterResource(Res.drawable.profile_placeholder),
                    contentDescription = "Place Holder",
                    contentScale = ContentScale.Crop
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )

        // Show progress indicator if -> picture is uploading || async image is loading
        if (updateProfilePictureState.isLoading) {
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
