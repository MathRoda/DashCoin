package com.mathroda.dashcoin.domain.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreSavedCoinState(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("savedCoin")
        val SAVED_COIN_KEY = booleanPreferencesKey("saved_coin")
    }

    val getSavedCoinState: Flow<Boolean> = context.dataStore.data
        .map {
            it[SAVED_COIN_KEY]?: false
        }

    suspend fun savedCoinState(state: Boolean) {
        context.dataStore.edit {
            it[SAVED_COIN_KEY] = state
        }
    }
}