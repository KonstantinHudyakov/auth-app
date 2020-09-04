package me.khudyakov.authapp.auth

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import me.khudyakov.authapp.*
import me.khudyakov.authapp.auth.AuthorizationResult.*
import me.khudyakov.authapp.main.AdminMainView
import me.khudyakov.authapp.main.UserMainView
import me.khudyakov.authapp.pass.CreatePasswordView
import tornadofx.*

class AuthorizationView : View() {
    private val authorizationController: AuthorizationController by inject()

    private val name = SimpleStringProperty()
    private val password = SimpleStringProperty()

    override val root: Parent = form {
        fieldset("Авторизация") {
            field("Имя пользователя") {
                textfield(name) {
                    setMaxSize(450.0, 50.0)
                }
            }
            field("Пароль") {
                passwordfield(password) {
                    setMaxSize(450.0, 50.0)
                }
            }
            field {
                hbox {
                    button("Ок") {
                        action(this@AuthorizationView::handleAuthentication)
                        hboxConstraints {
                            marginTop = 50.0
                            marginLeft = 250.0
                        }
                    }
                    button("Выход") {
                        action { primaryStage.close() }
                        hboxConstraints {
                            marginTop = 50.0
                            marginLeft = 50.0
                        }
                    }
                }
            }
        }
    }

    private fun handleAuthentication() {
        val name = name.get()
        if (name == null) {
            openModalView<NameMustBeNotEmptyPopup>()
            return
        }
        val pass = password.get() ?: ""
        when (authorizationController.tryToAuthorize(name, pass)) {
            PASSWORD_IS_NOT_SPECIFIED -> {
                openModalView<CreatePasswordView>()?.setOnHidden {
                    if(currentUser.password == "") {
                        Platform.runLater { close() }
                    }
                    switchToMainView()
                }
            }
            USER_NOT_FOUND -> {
                initFields()
                openModalView<UserNotFoundPopup>()
            }
            WRONG_PASSWORD -> {
                password.set("")
                openModalView<WrongPasswordPopup>()
            }
            ATTEMPTS_COUNT_LIMIT_EXCEEDED -> {
                password.set("")
                openModalView<AuthAttemptsLimitExceededPopup>()?.setOnHidden {
                    Platform.runLater { this.close() }
                }
            }
            USER_IS_BLOCKED -> {
                initFields()
                openModalView<UserIsBlockedPopup>()
            }
            AUTHORIZED -> {
                switchToMainView()
            }
        }
    }

    private fun switchToMainView() {
        if (isAdmin) {
            replaceWith<AdminMainView>()
        } else {
            replaceWith<UserMainView>()
        }
    }

    private fun initFields() {
        name.set("")
        password.set("")
    }
}
