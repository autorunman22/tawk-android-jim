package com.tawkto.jim.repository

import com.tawkto.jim.db.ProfileCacheMapper
import com.tawkto.jim.db.ProfileDao
import com.tawkto.jim.model.Profile
import com.tawkto.jim.retrofit.GithubService
import com.tawkto.jim.retrofit.ProfileNetMapper
import com.tawkto.jim.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val githubService: GithubService,
    private val profileNetworkNetMapper: ProfileNetMapper,
    private val profileDao: ProfileDao,
    private val profileCacheMapper: ProfileCacheMapper,
) {

    suspend fun userByName(name: String): Flow<DataState<Profile>> = flow {
        Timber.d("Fetching profile...")
        emit(DataState.Loading)

        try {
            val profileNetEntity = githubService.user(name)
            val profile = profileNetworkNetMapper.mapFromEntity(profileNetEntity)

            // Cache profile to room
            profileDao.insert(profileCacheMapper.mapToEntity(profile))
//            val profileEntity = ProfileNetworkEntity(
//                3,
//                "https://avatars.githubusercontent.com/u/3?v=4",
//                "PJ Simon",
//                "Facebook Inc",
//                "http://tawk.to/blog",
//                "LA San Francisco",
//                "pjhyatt@tawk.com",
//                3450,
//                120)

            emit(DataState.Success(profile))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}