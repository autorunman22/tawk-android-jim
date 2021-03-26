package com.tawkto.jim.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKeys: List<UserRemoteKeys>)

    @Query("SELECT * from userremotekeys WHERE id = :id")
    fun remoteKeysByImdbId(id: Int): UserRemoteKeys?

    @Query("DELETE from userremotekeys")
    fun clearRemoteKeys()

    @Query("SELECT * FROM userremotekeys")
    fun allKeys(): List<UserRemoteKeys>
}