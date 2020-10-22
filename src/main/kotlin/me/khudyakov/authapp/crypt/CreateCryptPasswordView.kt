package me.khudyakov.authapp.crypt

import javafx.beans.property.SimpleStringProperty
import me.khudyakov.authapp.PasswordMismatchPopup
import me.khudyakov.authapp.UsersManager
import me.khudyakov.authapp.auth.AuthorizationView
import me.khudyakov.authapp.openModalView
import tornadofx.*

class CreateCryptPasswordView : View() {
    private val usersManager: UsersManager by inject()

    private val passwordProperty = SimpleStringProperty()
    private val repeatPasswordProperty = SimpleStringProperty()

    override val root = form {
        fieldset {
            field {
                label("Задайте пароль для шифрования файла с учётными записями")
            }
            field("Пароль") {
                passwordfield(property = passwordProperty)
            }
            field("Повтор пароля") {
                passwordfield(property = repeatPasswordProperty)
            }
            field {
                button("Ок") {
                    action(this@CreateCryptPasswordView::handlePasswordEntered)
                }
                button("Выход") {
                    action(this@CreateCryptPasswordView::close)
                }
            }
        }
    }

    private fun handlePasswordEntered() {
        val pass = passwordProperty.get()
        val repeatPass = repeatPasswordProperty.get()
        if (pass == null || pass == "" || repeatPass != pass) {
            openModalView<PasswordMismatchPopup>()
            reset()
        } else {
            TripleDes.password = pass
            usersManager.initStorage()
            replaceWith<AuthorizationView>()
        }
    }

    override fun onUndock() {
        reset()
    }

    private fun reset() {
        passwordProperty.set("")
        repeatPasswordProperty.set("")
    }
}
