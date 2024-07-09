package com.mathroda.profile_screen

import android.graphics.Bitmap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.core.state.UserState
import com.mathroda.core.util.Resource
import com.mathroda.datasource.core.DashCoinRepository
import com.mathroda.datasource.firebase.FirebaseRepository
import com.mathroda.datasource.usecases.DashCoinUseCases
import com.mathroda.domain.model.DashCoinUser
import com.mathroda.domain.model.FavoriteCoin
import com.mathroda.notifications.sync.SyncNotification
import com.mathroda.profile_screen.drawer.state.SyncState
import com.mathroda.profile_screen.drawer.state.UpdatePictureState
import com.mathroda.profile_screen.menuitem.MenuItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dashCoinUseCases: DashCoinUseCases,
    private val dashCoinRepository: DashCoinRepository,
    private val syncNotification: SyncNotification
) : ViewModel() {

    private val _userCredential = MutableStateFlow(DashCoinUser())
    val userCredential = _userCredential.asStateFlow()

    private val _authState = mutableStateOf<UserState>(UserState.UnauthedUser)
    val authState: State<UserState> = _authState

    private val _updateProfilePictureState = MutableStateFlow(UpdatePictureState())
    val updateProfilePictureState = _updateProfilePictureState.asStateFlow()

    private val _toastState = mutableStateOf(Pair(false, ""))
    val toastState = _toastState

    private val _isUserPremium = MutableStateFlow(false)
    val isUserPremium = _isUserPremium.asStateFlow()

    private val syncState = mutableStateOf<SyncState>(SyncState.UpToDate)

    fun init() {
        getUserCredential()
        updateUiState()
        isUserPremium()
        getIfSyncNeeded()
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            dashCoinUseCases.signOut()
            clearUpdateProfilePictureState()
            updateUiState()
        }
    }

    private fun getUserCredential() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = dashCoinRepository.getDashCoinUser()
            _userCredential.value = user ?: DashCoinUser()
        }
    }

    private fun updateUiState() {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = dashCoinUseCases.userStateProvider()
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
        _updateProfilePictureState.value = UpdatePictureState()
    }

    /*private fun uploadProfilePicture(imageName: String, bitmap: Bitmap) {
        viewModelScope.launch {
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
        viewModelScope.launch {
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
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
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
        coins: List<FavoriteCoin>
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
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.getFlowFavoriteCoins().collectLatest { cloudCoins ->
                if (cloudCoins.data.isNullOrEmpty()) {
                    syncState.value = SyncState.NeedSync(emptyList())
                }

                dashCoinRepository.getFlowFavoriteCoins().firstOrNull()?.let { localCoins ->
                    val cloud = cloudCoins.data?.map { it.coinId }
                    val local = localCoins.map { it.coinId }
                    if (cloud?.containsAll(local) == true) {
                        syncState.value = SyncState.UpToDate
                    } else {
                        syncState.value = SyncState.NeedSync(localCoins)
                    }
                }
            }
        }
    }

    private fun showSyncNotification() = syncNotification.show(
        id = SYNC_NOTIFY_ID,
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