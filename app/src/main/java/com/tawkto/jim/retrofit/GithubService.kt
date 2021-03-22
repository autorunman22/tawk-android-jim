package com.tawkto.jim.retrofit

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("/users")
    suspend fun users(@Query("since") since: Int? = 0): List<UserNetworkEntity>

    @GET("/users/{name}")
    suspend fun profile(@Path("name") name: String): ProfileNetworkEntity
}