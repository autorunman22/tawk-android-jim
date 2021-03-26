package com.tawkto.jim.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tawkto.jim.db.AppDatabase
import com.tawkto.jim.db.UserCacheEntity
import com.tawkto.jim.db.UserCacheMapper
import com.tawkto.jim.db.UserRemoteKeys
import com.tawkto.jim.retrofit.GithubService
import com.tawkto.jim.retrofit.NetworkMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@ExperimentalPagingApi
class UserRemoteMediator @Inject constructor(
    private val db: AppDatabase,
    private val githubService: GithubService,
    private val userNetworkMapper: NetworkMapper,
    private val userCacheMapper: UserCacheMapper,
) : RemoteMediator<Int, UserCacheEntity>() {

    private val userDao = db.getUserDao()
    private val remoteKeysDao = db.getRemoteKeysDao()

    override suspend fun initialize(): InitializeAction = withContext(Dispatchers.IO) {
        val users = userDao.users()
        if (users.isEmpty()) return@withContext InitializeAction.LAUNCH_INITIAL_REFRESH
        // We will assume that cache data is fresh all the time
        else return@withContext  InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserCacheEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val key = getRemoteKeysClosestToCurrentPosition(state)
                    Timber.d("REFRESH key: $key, output: ${key?.nextKey?.minus(1)}")
                    key?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val key = db.withTransaction {
                        db.getRemoteKeysDao().allKeys().lastOrNull()
                    }
                    Timber.d("APPEND key: $key")
                    key?.nextKey ?: return MediatorResult.Success(true)
                }
            }

            Timber.d("computed loadKey: $loadKey")

            val response = githubService.users(loadKey)
            val endOfPaginationReached = response.size < state.config.pageSize

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    userDao.clearAll()
                }

                val prevKey = if (loadKey == 1) null else loadKey - 1
                val nextKey = if (endOfPaginationReached) null else loadKey + 1
                val remoteKeys = response.map {
                    UserRemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeysDao.insertAll(remoteKeys)

                val users = userNetworkMapper.mapFromEntityList(response, listOf())
                userDao.insertAll(userCacheMapper.mapToEntityList(users))
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, UserCacheEntity>): UserRemoteKeys? {
        val anchorPosition = state.anchorPosition
        Timber.d("anchorPosition: $anchorPosition")
        return anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.let { imdbId ->
                db.withTransaction {
                    db.getRemoteKeysDao().remoteKeysByImdbId(imdbId)
                }
            }
        }
    }

}