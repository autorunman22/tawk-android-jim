package com.tawkto.jim.retrofit

import retrofit2.http.GET

interface GithubService {

    @GET("/users")
    suspend fun users(): List<UserNetworkEntity>
}