package com.tawkto.jim.ui

import androidx.lifecycle.*
import com.tawkto.jim.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val profileRepository: ProfileRepository): ViewModel() {

    val query = MutableLiveData("")

    val results = query.switchMap {
        profileRepository.getProfileByQuery(it)
    }
}