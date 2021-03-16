package com.tawkto.jim.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserCacheEntity::class], version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {
        val DB_NAME = "tawkto_db"
    }
}