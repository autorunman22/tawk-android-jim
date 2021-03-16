package com.tawkto.jim.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("select * from users")
    fun users(): Flow<List<UserCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userCacheEntity: UserCacheEntity): Long
}