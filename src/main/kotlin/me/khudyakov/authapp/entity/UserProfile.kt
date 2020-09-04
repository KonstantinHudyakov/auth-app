package me.khudyakov.authapp.entity

import java.io.Serializable

data class UserProfile(
    val name: String,
    var password: String = "",
    var isBlocked: Boolean = false,
    var isOnPasswordRestrictions: Boolean = true
) : Serializable {
    companion object {
        private const val serialVersionUID = 2943262553179140901L
    }
}