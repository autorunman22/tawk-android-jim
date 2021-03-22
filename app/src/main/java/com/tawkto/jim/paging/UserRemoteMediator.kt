package com.tawkto.jim.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tawkto.jim.db.AppDatabase
import com.tawkto.jim.db.UserCacheEntity
import com.tawkto.jim.db.UserCacheMapper
import com.tawkto.jim.retrofit.GithubService
import com.tawkto.jim.retrofit.NetworkMapper
import javax.inject.Inject

@ExperimentalPagingApi
class UserRemoteMediator @Inject constructor(
    private val db: AppDatabase,
    private val githubService: GithubService,
    private val userNetworkMapper: NetworkMapper,
    private val userCacheMapper: UserCacheMapper,
) : RemoteMediator<Int, UserCacheEntity>() {

    private val userDao = db.getUserDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, UserCacheEntity>): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.id
                }
            }

            val response = githubService.users(loadKey)

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDao.clearAll()
                }

                val users = userNetworkMapper.mapFromEntityList(response, listOf())
                userDao.insertAll(userCacheMapper.mapToEntityList(users))
            }
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}