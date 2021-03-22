package com.tawkto.jim.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawkto.jim.model.Profile
import com.tawkto.jim.model.User
import com.tawkto.jim.repository.ProfileRepository
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
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // To be collected and displayed in the ProfileActivity
    private val mMutableUser = MutableStateFlow<DataState<Profile>>(DataState.Initial)
    val user: StateFlow<DataState<Profile>> = mMutableUser

    // Tracks changes to the note field
    val note: MutableLiveData<String?> = MutableLiveData(null)

    // Keep a reference of the current user to update note field
    private lateinit var mUser: Pair<Int, String>

    // Persist the note for this profile and user model
    fun onSave() {
        if (note.value == null) return

        Timber.d("Saving this note: ${note.value}")
        viewModelScope.launch(Dispatchers.Default) {
            userRepository.updateNoteById(mUser.first, note.value)
            profileRepository.updateNoteById(mUser.first, note.value)
        }
    }

    fun userByUsername(userPair: Pair<Int, String>) {
        this.mUser = userPair

        viewModelScope.launch(Dispatchers.Default) {
            profileRepository.userByName(userPair.second, userPair.first).collect {
                mMutableUser.value = it
            }
        }
    }
}