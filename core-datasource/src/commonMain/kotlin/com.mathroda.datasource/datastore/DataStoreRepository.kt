package com.mathroda.datasource.datastore

import com.mathroda.cache.datastore.DashCoinDataStore

class DataStoreRepository(
    private val dataStore: DashCoinDataStore
) {

    val readOnBoardingState = dataStore.readOnBoardingState
    val readIsUserExistState = dataStore.readIsUserExistState
    val readNotificationPreference = dataStore.readNotificationPreference

    suspend fun saveOnBoardingState(completed: Boolean)  =
        dataStore.saveOnBoardingState(completed)

    suspend fun saveIsUserExist(exist: Boolean) =
        dataStore.saveIsUserExist(exist)

    suspend fun saveNotificationPreference(enabled: Boolean) =
        dataStore.saveNotificationPreference(enabled)

}