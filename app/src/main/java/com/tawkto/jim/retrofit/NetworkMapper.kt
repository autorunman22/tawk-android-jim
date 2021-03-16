package com.tawkto.jim.retrofit

import com.tawkto.jim.model.User
import com.tawkto.jim.util.Mapper
import javax.inject.Inject

class NetworkMapper @Inject constructor() : Mapper<UserNetworkEntity, User> {

    override fun mapFromEntity(entity: UserNetworkEntity): User {
        return User(
            id = entity.id,
            username = entity.username,
            avatarUrl = entity.avatarUrl,
        )
    }

    override fun mapToEntity(model: User): UserNetworkEntity {
        return UserNetworkEntity(
            id = model.id,
            username = model.username,
            avatarUrl = model.avatarUrl,
        )
    }

    fun mapFromEntityList(entities: List<UserNetworkEntity>): List<User> {
        return entities.map { mapFromEntity(it) }
    }
}