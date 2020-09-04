package me.khudyakov.authapp

import me.khudyakov.authapp.entity.UserProfile
import tornadofx.Controller

class UsersManager : Controller() {
    private val storage = UserStorage()

    val users: List<UserProfile>
        get() = storage.users

    fun saveUser(user: UserProfile) {
        val createdUser = findUserByName(user.name)
        if (createdUser == null) {
            storage.users.add(user)
        } else {
            storage.users.replaceAll { if (it.name == user.name) user else it }
        }
        storage.save()
    }

    fun clearAndAddAll(users: List<UserProfile>) {
        storage.initWith(users)
    }

    fun findUserByName(name: String): UserProfile? {
        return storage.users.find { it.name == name }?.copy()
    }
}