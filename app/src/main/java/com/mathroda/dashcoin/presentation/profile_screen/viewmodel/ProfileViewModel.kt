package com.mathroda.dashcoin.presentation.profile_screen.viewmodel

import androidx.lifecycle.ViewModel
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel(){

    val isCurrentUserExist = firebaseRepository.isCurrentUserExist()

    fun signOut() = firebaseRepository.signOut()

}