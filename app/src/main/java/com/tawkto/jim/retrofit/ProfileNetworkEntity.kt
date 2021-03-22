package com.tawkto.jim.retrofit

import com.google.gson.annotations.SerializedName

data class ProfileNetworkEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("company")
    val company: String?,
    @SerializedName("blog")
    val blog: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("following")
    val following: Int,
)