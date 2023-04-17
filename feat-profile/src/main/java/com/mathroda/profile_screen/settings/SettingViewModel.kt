package com.mathroda.profile_screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.profile_screen.settings.components.EnableNotificationsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private val _enableNotificationsState = MutableStateFlow(EnableNotificationsState())
    val enableNotificationsState = _enableNotificationsState.asStateFlow()

    fun init() {
       updateInitNotificationState()
    }

    private fun updateInitNotificationState() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataStoreRepository.readNotificationPreference.first()
            if (result) {
                _enableNotificationsState.update {
                    it.copy(
                        isEnabled = true,
                        title = "Notifications Enabled"
                    )
                }

                return@launch
            }

            _enableNotificationsState.update {
                it.copy(
                    isEnabled = false,
                    title = "Notifications Disabled"
                )
            }
        }
    }

    fun updateNotificationState(
        value: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveNotificationPreference(value)
        }

        if (value) {
            _enableNotificationsState.update {
                it.copy(
                    isEnabled = true,
                    title = "Notifications Enabled"
                )
            }

            return
        }

        _enableNotificationsState.update {
            it.copy(
                isEnabled = false,
                title = "Notifications Disabled"
            )
        }
    }

}