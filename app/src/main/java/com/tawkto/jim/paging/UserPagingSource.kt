package com.tawkto.jim.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tawkto.jim.retrofit.GithubService
import com.tawkto.jim.retrofit.UserNetworkEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPagingSource @Inject constructor(private val githubService: GithubService) :
    PagingSource<Int, UserNetworkEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserNetworkEntity> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = githubService.users(nextPageNumber)

            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserNetworkEntity>): Int? {
        return state.anchorPosition
    }
}