package com.tawkto.jim.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {

    @GET("/users")
    suspend fun users(): List<UserNetworkEntity>

    @GET("/users/{name}")
    suspend fun profile(@Path("name") name: String): ProfileNetworkEntity
}