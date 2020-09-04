package me.khudyakov.authapp

import me.khudyakov.authapp.auth.AuthorizationView
import me.khudyakov.authapp.entity.UserProfile
import me.khudyakov.authapp.userlist.UserListView
import tornadofx.App
import tornadofx.reloadStylesheetsOnFocus

lateinit var currentUser: UserProfile

val isAdmin: Boolean
    get() = ::currentUser.isInitialized && currentUser.name == "ADMIN"

class Application : App(AuthorizationView::class, AppStyle::class) {
    init {
        reloadStylesheetsOnFocus()
    }
}