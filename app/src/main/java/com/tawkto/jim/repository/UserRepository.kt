package com.tawkto.jim.repository

import android.util.Log
import com.tawkto.jim.model.User
import com.tawkto.jim.retrofit.GithubService
import com.tawkto.jim.retrofit.NetworkMapper
import com.tawkto.jim.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class UserRepository @Inject constructor(private val githubService: GithubService,
                                         private val networkMapper: NetworkMapper) {

    suspend fun getUsers(): Flow<DataState<List<User>>> = flow {
        Timber.d("Loading")
        emit(DataState.Loading)
        delay(2000)
        try {
            val networkUsers = githubService.users()
            val users = networkMapper.mapFromEntityList(networkUsers)
            Timber.d("users: $users")
            emit(DataState.Success(users))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}