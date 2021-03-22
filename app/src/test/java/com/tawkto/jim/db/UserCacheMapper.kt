package com.tawkto.jim.db

import com.tawkto.jim.util.TestUtil
import org.junit.Before
import org.junit.Assert.*
import org.junit.Test

class UserCacheMapperTest {
    private lateinit var userCacheMapper: UserCacheMapper

    @Before
    fun setup() {
        userCacheMapper = UserCacheMapper()
    }

    @Test
    fun `given user entity, when mapFromEntity, then should return user model`() {
        val userEntity = TestUtil.getTestUserCacheEntity()
        val user = userCacheMapper.mapFromEntity(userEntity)

        assertNotNull(user)
        assertEquals(userEntity.id, user.id)
        assertEquals(userEntity.username, user.username)
        assertEquals(userEntity.avatarUrl, user.avatarUrl)
        assertEquals(userEntity.type, user.type)
        assertEquals(userEntity.hasNote, user.hasNote)
    }

    @Test
    fun `given user model, when mapFromEntity, then should return user entity`() {
        val user = TestUtil.getTestUser()
        val userEntity = userCacheMapper.mapToEntity(user)

        assertNotNull(user)
        assertEquals(user.id, userEntity.id)
        assertEquals(user.username, userEntity.username)
        assertEquals(user.avatarUrl, userEntity.avatarUrl)
        assertEquals(user.type, userEntity.type)
        assertEquals(user.hasNote, userEntity.hasNote)
    }

    @Test
    fun `given list of user entities, when mapFromEntityList, should return list of users model`() {
        val entities = mutableListOf<UserCacheEntity>()
        repeat(20) {
            entities.add(TestUtil.getTestUserCacheEntity(it))
        }

        val users = userCacheMapper.mapFromEntityList(entities)

        assertEquals(entities.size, users.size)
    }
}