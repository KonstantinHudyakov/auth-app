package me.khudyakov.authapp.adduser

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import me.khudyakov.authapp.NameIsTakenPopup
import me.khudyakov.authapp.NameMustBeNotEmptyPopup
import me.khudyakov.authapp.UserSuccessfullyAddedPopup
import me.khudyakov.authapp.adduser.AddUserResult.USER_ADDED
import me.khudyakov.authapp.adduser.AddUserResult.USER_NAME_IS_TAKEN
import me.khudyakov.authapp.entity.UserProfile
import me.khudyakov.authapp.openModalView
import tornadofx.*

class AddUserView : View() {
    private val addUserController: AddUserController by inject()

    private val name = SimpleStringProperty()
    private val isBlocked = SimpleBooleanProperty()
    private val isOnPasswordRestrictions = SimpleBooleanProperty()

    override fun onBeforeShow() {
        initFields()
    }

    override val root: Parent = form {
        fieldset("Добавление пользователя") {
            field("Имя пользователя") {
                textfield(name) {
                    setMaxSize(450.0, 50.0)
                }
            }
            field("Заблокировать пользователя") {
                checkbox(property = isBlocked)
            }
            field("Ограничить выбор паролей") {
                checkbox(property = isOnPasswordRestrictions)
            }
            field {
                hbox {
                    button("Ок") {
                        action(this@AddUserView::handleAddUser)
                        hboxConstraints {
                            marginTop = 50.0
                            marginLeft = 250.0
                        }
                    }
                    button("Отмена") {
                        action(this@AddUserView::close)
                        hboxConstraints {
                            marginTop = 50.0
                            marginLeft = 50.0
                        }
                    }
                }
            }
        }
    }

    private fun handleAddUser() {
        val name = name.get()
        val isBlocked = isBlocked.get()
        val isOnPasswordRestrictions = isOnPasswordRestrictions.get()
        if (name == null || name == "") {
            this.name.set("")
            openModalView<NameMustBeNotEmptyPopup>()
        } else {
            val result = addUserController.tryToAddUser(
                UserProfile(name, "", isBlocked, isOnPasswordRestrictions)
            )
            if (result == USER_NAME_IS_TAKEN) {
                this.name.set("")
                openModalView<NameIsTakenPopup>()
            } else if (result == USER_ADDED) {
                openModalView<UserSuccessfullyAddedPopup>()?.setOnHidden {
                    initFields()
                }
            }
        }
    }

    private fun initFields() {
        name.set("")
        isBlocked.set(false)
        isOnPasswordRestrictions.set(true)
    }
}