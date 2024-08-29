package com.mathroda.datasource.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.mathroda.network.utils.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

internal const val dataStoreFileName = "dashcoin.preferences_pb"

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )


class DataStoreRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val ON_BOARDING_KEY = booleanPreferencesKey("on_boarding_completed")
        val USER_EXIST_KEY = booleanPreferencesKey("is_user_exist")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("is_notifications_enabled")
    }

    val readOnBoardingState: Flow<Boolean> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {
                it[ON_BOARDING_KEY] ?: false
            }

    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit {
            it[ON_BOARDING_KEY] = completed
        }
    }

    val readIsUserExistState: Flow<Boolean> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {
                it[USER_EXIST_KEY] ?: false
            }

    suspend fun saveIsUserExist(exist: Boolean) {
        dataStore.edit {
            it[USER_EXIST_KEY] = exist
        }
    }

    val readNotificationPreference: Flow<Boolean> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {
                it[NOTIFICATIONS_ENABLED] ?: true
            }

    suspend fun saveNotificationPreference(enabled: Boolean) {
        dataStore.edit {
            it[NOTIFICATIONS_ENABLED] = enabled
        }
    }

}