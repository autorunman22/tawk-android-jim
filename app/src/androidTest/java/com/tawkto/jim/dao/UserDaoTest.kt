package com.tawkto.jim.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tawkto.jim.db.AppDatabase
import com.tawkto.jim.db.UserCacheEntity
import com.tawkto.jim.db.UserDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        userDao = db.getUserDao()
    }

    @Test
    fun given_userCacheEntity_when_inserted_then_should_return_same_id() {
        runBlocking {
            val userCacheEntity = UserCacheEntity(1, "neutron", "http://avatar.com/dQdF", "admin", false)
            val id = userDao.insert(userCacheEntity)

            assertEquals(userCacheEntity.id, id.toInt())
        }
    }

    @Test
    fun given_list_of_userCacheEntity_when_inserted_then_should_match_list_size() {
        runBlocking {
            val itemCount = 10

            repeat(itemCount) {
                val userCacheEntity = UserCacheEntity(it, "neutron", "http://avatar.com/dQdF", "admin", false)
                userDao.insert(userCacheEntity)
            }

            val userCacheEntities = userDao.users()

            assertNotNull(userCacheEntities)
            assertEquals(itemCount, userCacheEntities.size)
        }
    }

    @Test
    fun given_a_userCacheEntity_when_update_note_should_update_db() {
        runBlocking {
            val id = 10

            // Given this userCacheEntity
            val userCacheEntity = UserCacheEntity(id, "neutron", "http://avatar.com/dQdF", "admin", false)
            userDao.insert(userCacheEntity)

            // Update its note flag
            userDao.updateNoteById(id, true)

            // Get the only row from the db
            val user = userDao.users().first()

            assertNotNull(user)
            assertTrue(user.hasNote)
        }
    }

    @After
    fun close() {
        db.close()
    }
}