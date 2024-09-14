package com.mathroda.profile.settings

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mathroda.core.util.AppInfo
import com.mathroda.datasource.datastore.DataStoreRepository
import com.mathroda.profile.settings.components.EnableNotificationsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingViewModel (
    private val dataStoreRepository: DataStoreRepository,
    private val appInfo: AppInfo
): ScreenModel {

    private val _enableNotificationsState = MutableStateFlow(EnableNotificationsState())
    val enableNotificationsState = _enableNotificationsState.asStateFlow()

    fun init() {
       updateInitNotificationState()
    }

    private fun updateInitNotificationState() {
        screenModelScope.launch(Dispatchers.IO) {
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
        screenModelScope.launch(Dispatchers.IO){
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

    fun getAppVersion() = appInfo.getVersion()

}