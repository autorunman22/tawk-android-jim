package com.tawkto.jim.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tawkto.jim.model.User
import com.tawkto.jim.retrofit.GithubService
import com.tawkto.jim.retrofit.NetworkMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPagingSource @Inject constructor(private val githubService: GithubService, private val networkMapper: NetworkMapper) :
    PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = githubService.users(nextPageNumber)

            LoadResult.Page(
                data = networkMapper.mapFromEntityList(response, listOf()),
                prevKey = null,
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition
    }
}