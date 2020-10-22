package me.khudyakov.authapp

import me.khudyakov.authapp.crypt.CreateCryptPasswordView
import me.khudyakov.authapp.crypt.EnterCryptPasswordView
import me.khudyakov.authapp.entity.UserProfile
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.importStylesheet
import tornadofx.reloadStylesheetsOnFocus
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

lateinit var currentUser: UserProfile

val isAdmin: Boolean
    get() = ::currentUser.isInitialized && currentUser.name == "ADMIN"

class Application : App() {
    override val primaryView: KClass<out UIComponent>
        get() {
            val usersFile = File("users.txt")
            return if (usersFile.exists()) {
                EnterCryptPasswordView::class
            } else {
                CreateCryptPasswordView::class
            }
        }

    init {
        importStylesheet(AppStyle::class)
        reloadStylesheetsOnFocus()
    }
}