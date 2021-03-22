package com.tawkto.jim.db

import com.tawkto.jim.model.User
import com.tawkto.jim.util.Mapper
import javax.inject.Inject

class UserCacheMapper @Inject constructor(): Mapper<UserCacheEntity, User> {

    override fun mapFromEntity(entity: UserCacheEntity): User {
        return User(
            id = entity.id,
            username = entity.username,
            avatarUrl = entity.avatarUrl,
            type = entity.type,
            hasNote = entity.hasNote,
        )
    }

    override fun mapToEntity(model: User): UserCacheEntity {
        return UserCacheEntity(
            id = model.id,
            username = model.username,
            avatarUrl = model.avatarUrl,
            type = model.type,
            hasNote = model.hasNote
        )
    }

    fun mapFromEntityList(entities: List<UserCacheEntity>): List<User> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(users: List<User>): List<UserCacheEntity> {
        return users.map {
            mapToEntity(it)
        }
    }
}