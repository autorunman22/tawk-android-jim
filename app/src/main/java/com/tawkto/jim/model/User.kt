package com.tawkto.jim.model

import java.io.Serializable

data class User(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val type: String,
    var hasNote: Boolean,
) : Serializable