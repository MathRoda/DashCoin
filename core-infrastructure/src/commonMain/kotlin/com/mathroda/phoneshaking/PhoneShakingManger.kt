package com.mathroda.phoneshaking

import kotlinx.coroutines.flow.Flow

sealed class PhoneShakingState {
    data object IDLE: PhoneShakingState()
    data object IsShaking: PhoneShakingState()
}
interface PhoneShakingManger {
    fun getState(): Flow<PhoneShakingState>
}