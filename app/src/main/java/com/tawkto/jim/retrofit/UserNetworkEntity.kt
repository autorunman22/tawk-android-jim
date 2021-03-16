package com.tawkto.jim.retrofit

import com.google.gson.annotations.SerializedName

class UserNetworkEntity(
    @SerializedName("id")
    var id: Int,
    @SerializedName("login")
    var username: String,
    @SerializedName("avatar_url")
    var avatarUrl: String
)