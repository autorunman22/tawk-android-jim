package com.tawkto.jim.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserCacheEntity::class, ProfileCacheEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getProfileDao(): ProfileDao

    companion object {
        val DB_NAME = "tawkto_db"
    }
}