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

    private val mNavToProfile = MutableSharedFlow<User>(replay = 1)
    val navToProfile: SharedFlow<User> = mNavToProfile

    fun onClickProfile(id: Int) {
        Timber.d("Selecting user with ID: $id")
        val user = (mUsers.value as DataState.Success).data.find { it.id == id }
        user?.let {
            mNavToProfile.tryEmit(it)
        }
    }
}