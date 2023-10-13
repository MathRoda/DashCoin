package com.mathroda.phoneshaking

import kotlinx.coroutines.flow.Flow

sealed class PhoneShakingState {
    object IDLE: PhoneShakingState()
    object IsShaking: PhoneShakingState()
}
interface PhoneShakingManger {
    fun getState(): Flow<PhoneShakingState>
}