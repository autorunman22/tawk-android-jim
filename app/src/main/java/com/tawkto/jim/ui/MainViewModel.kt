package com.tawkto.jim.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.tawkto.jim.db.UserCacheEntity
import com.tawkto.jim.db.UserDao
import com.tawkto.jim.model.User
import com.tawkto.jim.paging.UserRemoteMediator
import com.tawkto.jim.repository.ProfileRepository
import com.tawkto.jim.repository.UserRepository
import com.tawkto.jim.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @ExperimentalPagingApi
@Inject constructor(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    remoteMediator: UserRemoteMediator,
    private val userDao: UserDao,
) : ViewModel() {

    @ExperimentalPagingApi
    val flowPager: StateFlow<PagingData<UserCacheEntity>> = Pager(
        config = PagingConfig(pageSize = 30),
        remoteMediator = remoteMediator
    ) {
        userDao.pagingSource()
    }.flow.cachedIn(viewModelScope).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PagingData.empty())

    private val mUsers = MutableStateFlow<DataState<List<User>>>(DataState.Initial)
    val users: StateFlow<DataState<List<User>>> = mUsers

    suspend fun hasNote(id: Int): Boolean = withContext(Dispatchers.Default) {
        return@withContext profileRepository.profileHasNote(id)
    }

    // For version without paging
    fun loadUsers() {
        viewModelScope.launch(Dispatchers.Default) {
            userRepository.getUsers().collect {
                mUsers.value = it
            }
        }
    }
}