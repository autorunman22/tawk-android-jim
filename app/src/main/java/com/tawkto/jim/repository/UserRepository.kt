package com.tawkto.jim.repository

import com.tawkto.jim.db.CacheMapper
import com.tawkto.jim.db.UserDao
import com.tawkto.jim.model.User
import com.tawkto.jim.retrofit.GithubService
import com.tawkto.jim.retrofit.NetworkMapper
import com.tawkto.jim.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val githubService: GithubService,
    private val networkMapper: NetworkMapper,
    private val userDao: UserDao,
    private val cacheMapper: CacheMapper
) {

    suspend fun getUsers(): Flow<DataState<List<User>>> = flow {
        Timber.d("Fetching users...")
        emit(DataState.Loading)

        Timber.d("Show cached users if any")
        val dbUsers = cacheMapper.mapFromEntityList(userDao.users())
        emit(DataState.Success(dbUsers))

        try {
            val networkUsers = githubService.users()
            val users = networkMapper.mapFromEntityList(networkUsers)

            // Cache users to Room
            for (user in users) {
                userDao.insert(cacheMapper.mapToEntity(user))
            }

            emit(DataState.Success(users))
            Timber.d("Done")
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}