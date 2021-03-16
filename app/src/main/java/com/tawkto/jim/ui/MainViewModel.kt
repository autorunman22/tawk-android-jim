package com.tawkto.jim.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawkto.jim.model.User
import com.tawkto.jim.repository.UserRepository
import com.tawkto.jim.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    private val mUsers = MutableStateFlow<DataState<List<User>>>(DataState.Initial)
    val users: StateFlow<DataState<List<User>>> = mUsers

    init {
        viewModelScope.launch(Dispatchers.Default) {
            userRepository.getUsers().collect {
                mUsers.value = it
            }
        }
    }

    fun onClickProfile(id: Int) {
        Timber.d("Loading profile")
    }
}