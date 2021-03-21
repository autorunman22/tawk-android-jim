package com.tawkto.jim.ui

import androidx.lifecycle.MutableLiveData
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository): ViewModel() {

    private val mUser = MutableStateFlow<DataState<Profile>>(DataState.Initial)
    val user: StateFlow<DataState<Profile>> = mUser

    val note: MutableLiveData<String?> = MutableLiveData(null)

    // Persist the note for this profile
    fun onSave() {
        if (note.value == null) return

        val mNote = note.value!!
        Timber.d("Saving this note: $mNote")
    }

    fun userByName(name: String, id: Int) {

        viewModelScope.launch(Dispatchers.Default) {
            repository.userByName(name, id).collect {
                mUser.value = it
            }
        }
    }
}