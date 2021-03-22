package com.tawkto.jim.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tawkto.jim.util.TestUtil
import com.tawkto.jim.db.AppDatabase
import com.tawkto.jim.db.ProfileDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileDaoTest {
    private lateinit var profileDao: ProfileDao
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        profileDao = db.getProfileDao()
    }

    @Test
    fun given_profileCacheEntity_when_inserted_then_should_return_same_id() {
        runBlocking {
            val profileCacheEntity = TestUtil.getTestProfileCacheEntity()
            val id = profileDao.insert(profileCacheEntity)

            assertEquals(profileCacheEntity.id, id.toInt())
        }
    }

    @Test
    fun given_list_of_profileCacheEntity_when_inserted_then_should_match_list_size() {
        runBlocking {
            val itemCount = 10

            repeat(itemCount) {
                val profileCacheEntity = TestUtil.getTestProfileCacheEntity(it)
                profileDao.insert(profileCacheEntity)
            }

            val profileCacheEntity = profileDao.profiles()

            assertNotNull(profileCacheEntity)
            assertEquals(itemCount, profileCacheEntity.size)
        }
    }

    @Test
    fun given_id_when_getProfileById_then_should_return_object() {
        runBlocking {
            val id = 1
            val profileCacheEntity = TestUtil.getTestProfileCacheEntity(id)
            profileDao.insert(profileCacheEntity)

            // Given existing ID, should not be null
            val retrievedProfileCacheEntity = profileDao.getProfileById(id)
            assertNotNull(retrievedProfileCacheEntity)

            // Assert that given entity must match retrieved entity
            assertEquals(profileCacheEntity.id, retrievedProfileCacheEntity?.id)
            assertEquals(profileCacheEntity.name, retrievedProfileCacheEntity?.name)
            assertEquals(profileCacheEntity.avatarUrl, retrievedProfileCacheEntity?.avatarUrl)
            assertEquals(profileCacheEntity.company, retrievedProfileCacheEntity?.company)
            assertEquals(profileCacheEntity.blog, retrievedProfileCacheEntity?.blog)
            assertEquals(profileCacheEntity.location, retrievedProfileCacheEntity?.location)
            assertEquals(profileCacheEntity.email, retrievedProfileCacheEntity?.email)
            assertEquals(profileCacheEntity.followers, retrievedProfileCacheEntity?.followers)
            assertEquals(profileCacheEntity.following, retrievedProfileCacheEntity?.following)
            assertEquals(profileCacheEntity.note, retrievedProfileCacheEntity?.note)

            // Given non-existing ID, should be null
            val retrievedProfileCacheEntity2 = profileDao.getProfileById(100)
            assertNull(retrievedProfileCacheEntity2)
        }
    }

    @After
    fun close() {
        db.close()
    }
}