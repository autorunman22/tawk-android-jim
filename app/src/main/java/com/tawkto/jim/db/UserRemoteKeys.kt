package com.tawkto.jim.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserRemoteKeys(
    @PrimaryKey
    val id: Int,
    val prevKey: Int?,
    val nextKey: Int?,
)