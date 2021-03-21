package com.tawkto.jim.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProfileDao {

    @Query("select * from profiles")
    fun profiles(): List<ProfileCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profile: ProfileCacheEntity): Long
}