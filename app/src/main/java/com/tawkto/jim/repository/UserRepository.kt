package com.tawkto.jim.repository

import com.tawkto.jim.db.UserCacheMapper
import com.tawkto.jim.db.UserDao
import com.tawkto.jim.model.User
import com.tawkto.jim.retrofit.GithubService
import com.tawkto.jim.retrofit.NetworkMapper
import com.tawkto.jim.util.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val githubService: GithubService,
    private val networkMapper: NetworkMapper,
    private val userDao: UserDao,
    private val userCacheMapper: UserCacheMapper
) {

    /**
     * Emits a flow by fetching users from /users endpoint
     * */
    suspend fun getUsers(): Flow<DataState<List<User>>> = flow {
        Timber.d("Fetching users list...")
        emit(DataState.Loading)

        Timber.d("Show cached users if any")
        val mUsers = userCacheMapper.mapFromEntityList(userDao.users())
        Timber.d("Cached users: ${mUsers.size}")
        emit(DataState.Success(mUsers))

        try {
            delay(1000)
            val networkUsers = githubService.users()
            val users = networkMapper.mapFromEntityList(networkUsers, mUsers)

            // Cache users to Room
            for (user in users) {
                userDao.insert(userCacheMapper.mapToEntity(user))
            }

            emit(DataState.Success(users))
            Timber.d("Done")
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    fun updateNoteById(id: Int, note: String?) {
        userDao.updateNoteById(id, note != null)
    }
}