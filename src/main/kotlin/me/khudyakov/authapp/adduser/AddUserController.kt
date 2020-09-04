package me.khudyakov.authapp.adduser

import me.khudyakov.authapp.UsersManager
import me.khudyakov.authapp.adduser.AddUserResult.USER_ADDED
import me.khudyakov.authapp.adduser.AddUserResult.USER_NAME_IS_TAKEN
import me.khudyakov.authapp.entity.UserProfile
import tornadofx.Controller

class AddUserController : Controller() {
    private val usersManager: UsersManager by inject()

    fun tryToAddUser(user: UserProfile): AddUserResult {
        return if(usersManager.findUserByName(user.name) == null) {
            usersManager.saveUser(user)
            USER_ADDED
        } else {
            USER_NAME_IS_TAKEN
        }
    }
}

enum class AddUserResult {
    USER_ADDED, USER_NAME_IS_TAKEN
}