package me.khudyakov.authapp.pass

import me.khudyakov.authapp.auth.AuthorizationController
import me.khudyakov.authapp.currentUser
import me.khudyakov.authapp.pass.ChangePasswordResult.*
import tornadofx.Controller

class ChangePasswordController : Controller() {
    private val authorizationController: AuthorizationController by inject()

    fun tryToChangePassword(oldPassword: String, newPassword: String): ChangePasswordResult {
        return if (oldPassword != currentUser.decodedPassword) {
            OLD_PASSWORD_IS_WRONG
        } else if (currentUser.isOnPasswordRestrictions && !authorizationController.isPasswordValid(newPassword)) {
            NEW_PASSWORD_DOESNT_MEET_THE_REQUIREMENTS
        } else {
            authorizationController.changeCurUserPassword(newPassword)
            PASSWORD_CHANGED
        }
    }
}

enum class ChangePasswordResult {
    PASSWORD_CHANGED,
    OLD_PASSWORD_IS_WRONG,
    NEW_PASSWORD_DOESNT_MEET_THE_REQUIREMENTS
}