package com.mathroda.dashcoin.presentation.profile_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.mathroda.dashcoin.core.util.Resource
import com.mathroda.dashcoin.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel(){

    fun signOut() = firebaseRepository.signOut()

}