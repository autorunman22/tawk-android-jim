package com.tawkto.jim.db

import com.tawkto.jim.model.Profile
import com.tawkto.jim.util.Mapper
import javax.inject.Inject

class ProfileCacheMapper @Inject constructor(): Mapper<ProfileCacheEntity, Profile> {

    override fun mapFromEntity(entity: ProfileCacheEntity): Profile {
        return Profile(
            id = entity.id,
            username = entity.username,
            name = entity.name,
            avatarUrl = entity.avatarUrl,
            company = entity.company,
            blog = entity.blog,
            location = entity.location,
            email = entity.email,
            followers = entity.followers,
            following = entity.following,
            note = entity.note,
        )
    }

    override fun mapToEntity(model: Profile): ProfileCacheEntity {
        return ProfileCacheEntity(
            id = model.id,
            username = model.username,
            name = model.name,
            avatarUrl = model.avatarUrl,
            company = model.company,
            blog = model.blog,
            location = model.location,
            email = model.email,
            followers = model.followers,
            following = model.following,
            note = model.note,
        )
    }

    fun mapFromEntityList(profiles: List<ProfileCacheEntity>): List<Profile> {
        return profiles.map {
            mapFromEntity(it)
        }
    }

}