package me.khudyakov.authapp.pass

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import me.khudyakov.authapp.PasswordDoesntMeetTheRequirementsPopup
import me.khudyakov.authapp.PasswordMismatchPopup
import me.khudyakov.authapp.PasswordMustBeNotEmptyPopup
import me.khudyakov.authapp.openModalView
import me.khudyakov.authapp.pass.ChangePasswordResult.NEW_PASSWORD_DOESNT_MEET_THE_REQUIREMENTS
import me.khudyakov.authapp.pass.ChangePasswordResult.PASSWORD_CHANGED
import tornadofx.*

class CreatePasswordView : View() {
    private val changePasswordController: ChangePasswordController by inject()

    private val password = SimpleStringProperty()
    private val repeatPassword = SimpleStringProperty()

    override val root: Parent = form {
        fieldset("Задайте пароль") {
            field("Пароль") {
                passwordfield(password) {
                    setMaxSize(450.0, 50.0)
                }
            }
            field("Подтверждение") {
                passwordfield(repeatPassword) {
                    setMaxSize(450.0, 50.0)
                }
            }
            field {
                hbox {
                    button("Ок") {
                        action(this@CreatePasswordView::handlePasswordSetting)
                        hboxConstraints {
                            marginTop = 50.0
                            marginLeft = 250.0
                        }
                    }
                    button("Выход") {
                        action {
                            primaryStage.close()
                        }
                        hboxConstraints {
                            marginTop = 50.0
                            marginLeft = 50.0
                        }
                    }
                }
            }
        }
    }

    private fun handlePasswordSetting() {
        val pass = password.get()
        val repeatPass = repeatPassword.get()
        if (pass == null || repeatPass == null || pass == "" || repeatPass == "") {
            openModalView<PasswordMustBeNotEmptyPopup>()
        } else if (pass != repeatPass) {
            openModalView<PasswordMismatchPopup>()
        } else {
            val result = changePasswordController.tryToChangePassword("", pass)
            if (result == NEW_PASSWORD_DOESNT_MEET_THE_REQUIREMENTS) {
                openModalView<PasswordDoesntMeetTheRequirementsPopup>()
            } else if (result == PASSWORD_CHANGED) {
                close()
            }
        }
        initFields()
    }

    private fun initFields() {
        password.set("")
        repeatPassword.set("")
    }
}