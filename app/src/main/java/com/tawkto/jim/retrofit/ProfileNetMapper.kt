package com.tawkto.jim.retrofit

import com.tawkto.jim.model.Profile
import com.tawkto.jim.util.Mapper
import javax.inject.Inject

class ProfileNetMapper @Inject constructor() : Mapper<ProfileNetworkEntity, Profile> {
    override fun mapFromEntity(entity: ProfileNetworkEntity): Profile {
        return Profile(
            id = entity.id,
            name = entity.name,
            avatarUrl = entity.avatarUrl,
            company = entity.company,
            blog = entity.blog,
            location = entity.location,
            email = entity.email,
            followers = entity.followers,
            following = entity.following,
            note = null,
        )
    }

    override fun mapToEntity(model: Profile): ProfileNetworkEntity {
        return ProfileNetworkEntity(
            id = model.id,
            name = model.name,
            avatarUrl = model.avatarUrl,
            company = model.company,
            blog = model.blog,
            location = model.location,
            email = model.email,
            followers = model.followers,
            following = model.following,
        )
    }


}