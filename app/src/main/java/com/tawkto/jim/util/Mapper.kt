package com.tawkto.jim.util

interface Mapper<E, M> {

    fun mapFromEntity(entity: E): M

    fun mapToEntity(model: M): E
}