package com.tawkto.jim.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserCacheEntity::class, ProfileCacheEntity::class, UserRemoteKeys::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getProfileDao(): ProfileDao
    abstract fun getRemoteKeysDao(): UserRemoteKeysDao

    companion object {
        const val DB_NAME = "tawkto_db"
    }
}