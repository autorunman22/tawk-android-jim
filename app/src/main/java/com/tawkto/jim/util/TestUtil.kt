package com.tawkto.jim.util

import com.tawkto.jim.db.ProfileCacheEntity
import com.tawkto.jim.db.UserCacheEntity
import com.tawkto.jim.model.Profile
import com.tawkto.jim.model.User

object TestUtil {

    fun getTestProfileCacheEntity(id: Int = 1): ProfileCacheEntity {
        return ProfileCacheEntity(id, "neutt", "Jim", "http://avatar.com/dQdF", null,
            "http://blog.com/jim", null, null, 150, 120,
            "simple note")
    }

    fun getTestProfile(id: Int = 1): Profile = Profile(id, "neutt","Jim",
        "http://avatar.com/dQdF", "tawk", "http://blog.com/jim", null,
        "jim.ovejera@gmail.com", 120, 2, "simple note")

    fun getTestUserCacheEntity(id: Int = 1): UserCacheEntity {
        return UserCacheEntity(id, "Jim", "http://avatar.com/dQdF", "admin", false)
    }

    fun getTestUser(id: Int = 1): User = User(id, "jim", "http://avatar.com/dQdF", "admin", true)
}