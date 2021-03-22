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

    /**
     * Emits a flow by fetching specific profile from /users/{name}
     *
     * @param name Name slug input for the endpoint
     * @param id Profile ID used for offline mode (if profile is cached already)
     * */
    suspend fun userByName(name: String, id: Int): Flow<DataState<Profile>> = flow {
        Timber.d("Fetching profile...")
        emit(DataState.Loading)

        val profileDb = profileDao.getProfileById(id)?.let { profileCacheMapper.mapFromEntity(it) }
        profileDb?.let {
            emit(DataState.Success(profileDb))
        }

        try {
            val profileNetEntity = githubService.profile(name)
            val profile = profileNetworkNetMapper.mapFromEntity(profileNetEntity).apply {
                note = profileDb?.note
            }

            // Cache profile to room
            profileDao.insert(profileCacheMapper.mapToEntity(profile))

            emit(DataState.Success(profile))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    fun updateNoteById(id: Int, note: String?) {
        profileDao.updateNoteById(id, note)
    }

    // Returns true if this profile has non-null note
    fun profileHasNote(id: Int): Boolean {
        val profileCache = profileDao.getProfileById(id) ?: return false

        return profileCache.note != null
    }
}