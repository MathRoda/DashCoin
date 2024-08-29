package com.mathroda.profile_screen

import android.graphics.Bitmap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mathroda.core.state.UserState
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.example.shared.DashCoinUser
import com.example.shared.FavoriteCoin
import com.mathroda.notifications.sync.SyncNotification
import com.mathroda.profile_screen.drawer.state.SyncState
import com.mathroda.profile_screen.drawer.state.UpdatePictureState
import com.mathroda.profile_screen.menuitem.MenuItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ProfileViewModel(
    private val firebaseRepository: FirebaseRepository,
    private val dashCoinUseCases: DashCoinUseCases,
    private val dashCoinRepository: DashCoinRepository,
    private val syncNotification: SyncNotification
) : ScreenModel {

    private val _userCredential = MutableStateFlow(com.example.shared.DashCoinUser())
    val userCredential = _userCredential.asStateFlow()

    private val _authState = MutableStateFlow<UserState>(UserState.UnauthedUser)
    val authState = _authState.asStateFlow()

    private val _updateProfilePictureState = MutableStateFlow(UpdatePictureState())
    val updateProfilePictureState = _updateProfilePictureState.asStateFlow()

    private val _toastState = mutableStateOf(Pair(false, ""))
    val toastState = _toastState

    private val _isUserPremium = MutableStateFlow(false)
    val isUserPremium = _isUserPremium.asStateFlow()

    private val syncState = MutableStateFlow<SyncState>(SyncState.UpToDate)

    fun init() {
        updateUiState()
        isUserPremium()
        getIfSyncNeeded()
    }

    fun signOut() {
        screenModelScope.launch(Dispatchers.IO) {
            dashCoinUseCases.signOut()
            clearUpdateProfilePictureState()
            updateUiState()
        }
    }

    private fun updateUiState() {
        screenModelScope.launch(Dispatchers.IO) {
            val user = dashCoinRepository.getDashCoinUser()
            val authState = dashCoinUseCases.userStateProvider()
            withContext(Dispatchers.Main.immediate) {
                _userCredential.update { user ?: com.example.shared.DashCoinUser() }
                _authState.update { authState  }
            }
        }
    }

    fun updateProfilePicture(bitmap: Bitmap) {
        val imageName = userCredential.value.userUid ?: UUID.randomUUID().toString()

        /*uploadProfilePicture(
            imageName = imageName,
            bitmap = bitmap
        )*/
    }

    fun clearUpdateProfilePictureState() {
        _updateProfilePictureState.update {  UpdatePictureState() }
    }

    /*private fun uploadProfilePicture(imageName: String, bitmap: Bitmap) {
        screenModelScope.launch {
            firebaseRepository.uploadImageToCloud(
                name = imageName,
                bitmap = bitmap
            ).collect { uploadResult ->
                when(uploadResult) {
                    is Resource.Loading -> {
                        _updateProfilePictureState.value = UpdatePictureState(isLoading = true)
                    }
                    is Resource.Success -> {
                        val imageUrl = uploadResult.data ?: ""
                        updateProfilePicture(imageUrl = imageUrl)
                        firebaseRepository.getUserCredentials().collect { result ->
                            if (result is Resource.Success) {
                                result.data?.let {
                                    dashCoinRepository.cacheDashCoinUser(it)
                                }
                                getUserCredential()
                            }
                        }
                    }
                    is Resource.Error -> {
                        _updateProfilePictureState.value = UpdatePictureState(isFailure = true)
                    }
                }
            }
        }
    }*/

    /*private fun updateProfilePicture(imageUrl: String) {
        screenModelScope.launch {
            firebaseRepository
                .updateUserProfilePicture(imageUrl = imageUrl)
                .collect { result ->
                    when(result) {
                        is Resource.Loading -> {
                            _updateProfilePictureState.value = UpdatePictureState(isLoading = true)
                        }
                        is Resource.Success -> {
                            _updateProfilePictureState.value = UpdatePictureState(isSuccess = true)
                        }
                        is Resource.Error -> {
                            _updateProfilePictureState.value = UpdatePictureState(isFailure = true)
                        }
                    }
                }
        }
    }*/

    private fun isUserPremium() {
        screenModelScope.launch(Dispatchers.IO) {
            val isUserPremium = dashCoinRepository.isUserPremiumLocal()
            _isUserPremium.update { isUserPremium }
        }
    }

    fun getMenuListItems(): List<MenuItems> {
        val items = mutableListOf<MenuItems>()
        items.addAll(
            listOf(
                MenuItems(
                    id = "settings",
                    title = "Settings",
                    contentDescription = "Toggle Home",
                    icon = Icons.Default.Settings
                ),
                /*MenuItems(
                      id = "help",
                      title = "Help Center",
                      contentDescription = "Toggle About",
                      icon = Icons.Default.Help
                  ),*/
                MenuItems(
                    id = "about",
                    title = "About DashCoin",
                    contentDescription = "Toggle About",
                    icon = Icons.Default.Favorite
                )
            )
        )

        if (_authState.value is UserState.PremiumUser) {
            items.add(
                MenuItems(
                    id = "syncData",
                    title = "Sync data to cloud",
                    contentDescription = "Toggle Sync",
                    icon = Icons.Default.Sync
                )
            )
        }
        return items
    }

    fun onSyncClicked() {
        screenModelScope.launch(Dispatchers.IO) {
            when(val result = syncState.value){
                is SyncState.NeedSync -> syncCoinsToCloud(result.coins)
                is SyncState.UpToDate -> updateToastState(
                    showToast = true,
                    message = "Up to date"
                )
            }

        }
    }

    private suspend fun syncCoinsToCloud(
        coins: List<com.example.shared.FavoriteCoin>
    ) {
        if (coins.isEmpty()) {
            updateToastState(
                showToast = true,
                message = "Up to date"
            )
            return
        }

        coins.map { coin ->
            firebaseRepository.addCoinFavorite(coin).first()
        }

        showSyncNotification()
    }

    private fun getIfSyncNeeded() {
        screenModelScope.launch(Dispatchers.IO) {
            firebaseRepository.getFlowFavoriteCoins().collectLatest { cloudCoins ->
                if (cloudCoins.data.isNullOrEmpty()) {
                    syncState.update { SyncState.NeedSync(emptyList()) }
                }

                dashCoinRepository.getFlowFavoriteCoins().firstOrNull()?.let { localCoins ->
                    val cloud = cloudCoins.data?.map { it.coinId }
                    val local = localCoins.map { it.coinId }
                    if (cloud?.containsAll(local) == true) {
                        syncState.value = SyncState.UpToDate
                    } else {
                        syncState.update {  SyncState.NeedSync(localCoins) }
                    }
                }
            }
        }
    }

    private fun showSyncNotification() = syncNotification.show(
        title = "Up to date 🚀",
        description = "All coins has been synced"
    )

    fun updateToastState(
        showToast: Boolean,
        message: String
    ) {
        _toastState.value = Pair(showToast, message)
    }

    companion object {
        const val SYNC_NOTIFY_ID = 767676
    }
}