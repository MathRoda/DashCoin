package com.mathroda.profile_screen.drawer

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mathroda.common.theme.BackgroundBlue
import com.mathroda.common.theme.DashCoinTheme
import com.mathroda.common.theme.Gold
import com.mathroda.profile_screen.R

@Composable
fun DrawerHeader(
    welcomeUser: String?,
    userEmail: String?,
    userImage: String?,
    isUserImageLoading: Boolean,
    iconVisibility: Boolean,
    isUserAuthed: Boolean,
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

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .padding(bottom = 10.dp)
        ) {
            if (userImage == null) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.5f)
                        .aspectRatio(1f)
//                    .graphicsLayer(scaleY = 0.7f, scaleX = 0.7f)
                    ,
                    painter = painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = "Profile Placeholder",
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = userImage,
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.4f)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                )
            }

            if (isUserAuthed) {
                PickImageButton(
                    modifier = Modifier.padding(10.dp),
                    onPickImage = updateProfilePicture,
                )
            }
        }

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
                isUserImageLoading = true,
                iconVisibility = true,
                isUserAuthed = true,
                updateProfilePicture = {}
            )
        }
    }
}