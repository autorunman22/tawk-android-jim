package com.tawkto.jim.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "avatarUrl")
    val avatarUrl: String,
    @ColumnInfo(name = "company")
    val company: String?,
    @ColumnInfo(name = "blog")
    val blog: String?,
    @ColumnInfo(name = "location")
    val location: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "followers")
    val followers: Int,
    @ColumnInfo(name = "following")
    val following: Int,
    @ColumnInfo(name = "note")
    val note: String?,
)