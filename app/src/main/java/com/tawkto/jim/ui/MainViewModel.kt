package com.tawkto.jim.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawkto.jim.model.User
import com.tawkto.jim.repository.UserRepository
import com.tawkto.jim.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    private val mUsers = MutableStateFlow<DataState<List<User>>>(DataState.Initial)
    val users: StateFlow<DataState<List<User>>> = mUsers

    fun loadUsers() {
        viewModelScope.launch(Dispatchers.Default) {
            userRepository.getUsers().collect {
                mUsers.value = it
            }
        }
    }

    private val mNavToProfile = MutableSharedFlow<Pair<Int, String>>(replay = 1)
    val navToProfile: SharedFlow<Pair<Int, String>> = mNavToProfile

    fun onClickProfile(id: Int, username: String) {
        Timber.d("Selecting user with ID: $id and username: $username")
        mNavToProfile.tryEmit(id to username)
    }
}