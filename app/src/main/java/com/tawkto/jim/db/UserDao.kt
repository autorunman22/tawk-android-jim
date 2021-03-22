package com.tawkto.jim.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun users(): List<UserCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userCacheEntities: List<UserCacheEntity>)

    @Query("DELETE FROM users")
    suspend fun clearAll()

    @Query("SELECT * from users")
    fun pagingSource(): PagingSource<Int, UserCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userCacheEntity: UserCacheEntity): Long

    @Query("UPDATE users SET has_note = :hasNote WHERE id = :id")
    fun updateNoteById(id: Int, hasNote: Boolean)
}