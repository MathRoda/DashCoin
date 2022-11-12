package com.mathroda.datasource.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepository(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("on_boarding_pref")
        val ON_BOARDING_KEY = booleanPreferencesKey("on_boarding_completed")
    }

    val readOnBoardingState: Flow<Boolean> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {
                it[ON_BOARDING_KEY]?: false
            }

    suspend fun saveOnBoardingState(completed: Boolean) {
        context.dataStore.edit {
            it[ON_BOARDING_KEY] = completed
        }
    }
}