package com.tawkto.jim.repository

import com.tawkto.jim.retrofit.GithubService
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val githubService: GithubService) {
}