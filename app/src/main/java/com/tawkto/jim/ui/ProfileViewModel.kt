package com.tawkto.jim.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawkto.jim.model.Profile
import com.tawkto.jim.repository.ProfileRepository
import com.tawkto.jim.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository): ViewModel() {

    private val mUser = MutableStateFlow<DataState<Profile>>(DataState.Initial)
    val user: StateFlow<DataState<Profile>> = mUser

    fun userByName(name: String) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.userByName(name).collect {
                mUser.value = it
            }
        }
    }
}