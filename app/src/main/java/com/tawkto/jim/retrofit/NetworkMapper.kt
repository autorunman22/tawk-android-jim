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
            type = entity.type,
            hasNote = false,
        )
    }

    override fun mapToEntity(model: User): UserNetworkEntity {
        return UserNetworkEntity(
            id = model.id,
            username = model.username,
            avatarUrl = model.avatarUrl,
            type = model.type,
        )
    }

    fun mapFromEntityList(entities: List<UserNetworkEntity>, users: List<User>): List<User> {
        return entities.map {
            mapFromEntity(it).apply {
                val user = users.find { user -> user.id == this.id }
                if (user != null) {
                    hasNote = user.hasNote
                }
            }
        }
    }
}