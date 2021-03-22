package com.tawkto.jim.model

data class Profile(
    val id: Int,
    val username: String,
    val name: String?,
    val avatarUrl: String,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val followers: Int,
    val following: Int,
    var note: String?,
)