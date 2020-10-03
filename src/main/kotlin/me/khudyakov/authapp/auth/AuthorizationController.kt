package me.khudyakov.authapp.auth

import me.khudyakov.authapp.UsersManager
import me.khudyakov.authapp.auth.AuthorizationResult.*
import me.khudyakov.authapp.currentUser
import tornadofx.Controller

class AuthorizationController : Controller() {
    companion object {
        const val MAX_COUNT_OF_ATTEMPTS = 3
    }

    private val usersManager: UsersManager by inject()
    private var curCountOfAttempts = 0

    fun tryToAuthorize(name: String, password: String): AuthorizationResult {
        val user = usersManager.findUserByName(name) ?: return USER_NOT_FOUND
        return when {
            user.isBlocked -> USER_IS_BLOCKED
            user.password == "" -> {
                currentUser = user
                PASSWORD_IS_NOT_SPECIFIED
            }
            user.password == password -> {
                currentUser = user
                AUTHORIZED
            }
            else -> {
                curCountOfAttempts++
                if (curCountOfAttempts < MAX_COUNT_OF_ATTEMPTS) {
                    WRONG_PASSWORD
                } else {
                    ATTEMPTS_COUNT_LIMIT_EXCEEDED
                }
            }
        }
    }

    private val validPasswordRegex = Regex(""".*[а-яА-Я]+.*[a-zA-Z]+.*[0-9]+.*[а-яА-Я]+.*""")

    fun isPasswordValid(password: String): Boolean {
        return validPasswordRegex.matches(password)
    }

    fun changeCurUserPassword(newPassword: String) {
        currentUser.password = newPassword
        usersManager.saveUser(currentUser)
    }
}

enum class AuthorizationResult {
    AUTHORIZED,
    USER_IS_BLOCKED,
    PASSWORD_IS_NOT_SPECIFIED,
    USER_NOT_FOUND, WRONG_PASSWORD,
    ATTEMPTS_COUNT_LIMIT_EXCEEDED
}