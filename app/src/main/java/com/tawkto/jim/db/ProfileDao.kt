package com.tawkto.jim.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProfileDao {

    @Query("select * from profiles")
    fun profiles(): List<ProfileCacheEntity>

    @Query("select * from profiles where id = :id")
    fun getProfileById(id: Int): ProfileCacheEntity?

    @Query("UPDATE profiles SET note = :note WHERE id = :id")
    fun updateNoteById(id: Int, note: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profile: ProfileCacheEntity): Long
}