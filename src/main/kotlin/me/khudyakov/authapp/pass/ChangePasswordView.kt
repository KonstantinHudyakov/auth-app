package me.khudyakov.authapp.pass

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import me.khudyakov.authapp.*
import me.khudyakov.authapp.pass.ChangePasswordResult.*
import tornadofx.*

class ChangePasswordView : View() {
    private val changePasswordController: ChangePasswordController by inject()

    private val oldPassword = SimpleStringProperty()
    private val newPassword = SimpleStringProperty()
    private val repeatPassword = SimpleStringProperty()

    override fun onBeforeShow() {
        initFields()
    }

    override val root: Parent = form {
        fieldset("Изменение пароля") {
            field("Старый пароль") {
                passwordfield(oldPassword) {
                    setMaxSize(450.0, 50.0)
                }
            }
            field("Новый пароль") {
                passwordfield(newPassword) {
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
                        action(this@ChangePasswordView::handleChangePassword)
                        hboxConstraints {
                            marginTop = 50.0
                            marginLeft = 250.0
                        }
                    }
                    button("Отмена") {
                        action(this@ChangePasswordView::handleClose)
                        hboxConstraints {
                            marginTop = 50.0
                            marginLeft = 50.0
                        }
                    }
                }
            }
        }
    }

    private fun handleChangePassword() {
        val oldPass = oldPassword.get()
        val newPass = newPassword.get()
        val repeatPass = repeatPassword.get()
        if (oldPass == null || newPass == null || repeatPass == null
            || oldPass == "" || newPass == "" || repeatPass == ""
        ) {
            initFields()
            openModalView<PasswordMustBeNotEmptyPopup>()
        } else if (newPass != repeatPass) {
            initNewPassword()
            openModalView<PasswordMismatchPopup>()
        } else {
            when (changePasswordController.tryToChangePassword(oldPass, newPass)) {
                NEW_PASSWORD_DOESNT_MEET_THE_REQUIREMENTS -> {
                    initNewPassword()
                    openModalView<PasswordDoesntMeetTheRequirementsPopup>()
                }
                OLD_PASSWORD_IS_WRONG -> {
                    initFields()
                    openModalView<WrongOldPasswordPopup>()
                }
                PASSWORD_CHANGED -> {
                    handleClose()
                }
            }
        }
    }

    private fun handleClose() {
        initFields()
        close()
    }

    private fun initFields() {
        oldPassword.set("")
        initNewPassword()
    }

    private fun initNewPassword() {
        newPassword.set("")
        repeatPassword.set("")
    }
}