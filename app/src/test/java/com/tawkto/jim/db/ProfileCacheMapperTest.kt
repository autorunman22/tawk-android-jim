package com.tawkto.jim.db

import com.tawkto.jim.util.TestUtil
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class ProfileCacheMapperTest {

    private lateinit var profileCacheMapper: ProfileCacheMapper

    @Before
    fun setUp() {
        profileCacheMapper = ProfileCacheMapper()
    }

    @Test
    fun `given profile cache entity, when mapFromEntity, then should return profile model`() {
        val entity = TestUtil.getTestProfileCacheEntity()

        val profile = profileCacheMapper.mapFromEntity(entity)

        assertNotNull(profile)
        assertEquals(entity.id, profile.id)
        assertEquals(entity.name, profile.name)
        assertEquals(entity.avatarUrl, profile.avatarUrl)
        assertEquals(entity.company, profile.company)
        assertEquals(entity.blog, profile.blog)
        assertEquals(entity.location, profile.location)
        assertEquals(entity.email, profile.email)
        assertEquals(entity.followers, profile.followers)
        assertEquals(entity.following, profile.following)
        assertEquals(entity.note, profile.note)
    }

    @Test
    fun `given profile model, when mapToEntity, then should return profile cache entity`() {
        val profile = TestUtil.getTestProfile()

        val cacheEntity = profileCacheMapper.mapToEntity(profile)

        assertNotNull(cacheEntity)
        assertEquals(profile.id, cacheEntity.id)
        assertEquals(profile.username, cacheEntity.username)
        assertEquals(profile.name, cacheEntity.name)
        assertEquals(profile.avatarUrl, cacheEntity.avatarUrl)
        assertEquals(profile.company, cacheEntity.company)
        assertEquals(profile.blog, cacheEntity.blog)
        assertEquals(profile.location, cacheEntity.location)
        assertEquals(profile.email, cacheEntity.email)
        assertEquals(profile.followers, cacheEntity.followers)
        assertEquals(profile.following, cacheEntity.following)
        assertEquals(profile.note, cacheEntity.note)
    }
}