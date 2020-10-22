package me.khudyakov.authapp.crypt

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import me.khudyakov.authapp.PasswordMustBeNotEmptyPopup
import me.khudyakov.authapp.UsersManager
import me.khudyakov.authapp.WrongPasswordPopup
import me.khudyakov.authapp.auth.AuthorizationView
import me.khudyakov.authapp.openModalView
import tornadofx.*

class EnterCryptPasswordView : View() {
    private val usersManager: UsersManager by inject()

    private val passwordProperty = SimpleStringProperty()

    override val root = form {
        fieldset {
            field {
                label("Введите пароль для расширования файла с учётными записями")
            }
            field("Пароль") {
                passwordfield(property = passwordProperty)
            }
            field {
                button("Ок") {
                    action(this@EnterCryptPasswordView::handlePasswordEntered)
                }
                button("Выход") {
                    action(this@EnterCryptPasswordView::close)
                }
            }
        }
    }

    private fun handlePasswordEntered() {
        val pass = passwordProperty.get()
        if(pass == null || pass == "") {
            openModalView<PasswordMustBeNotEmptyPopup>()
            return
        }
        try {
            TripleDes.password = pass
            usersManager.initStorage()
            if(usersManager.findUserByName("ADMIN") == null) {
                throw Exception()
            }
        } catch (ex: Exception) {
            openModalView<WrongPasswordPopup>()?.setOnHidden {
                Platform.runLater(this::close)
            }
            return
        }
        replaceWith<AuthorizationView>()
    }

    override fun onUndock() {
        reset()
    }

    private fun reset() {
        passwordProperty.set("")
    }
}
