package me.khudyakov.authapp.userlist

import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.scene.Parent
import me.khudyakov.authapp.UsersManager
import me.khudyakov.authapp.entity.UserProfile
import me.khudyakov.authapp.openModalView
import tornadofx.*

class UserListView : View() {
    private val usersManager: UsersManager by inject()

    private val users: ObservableList<UserProfile>
    var isNeedToBeSaved: Boolean = false

    init {
        users = ArrayList(usersManager.users.map { it.copy() }).asObservable()
    }

    override fun onBeforeShow() {
        initTableData()
    }

    private fun initTableData() {
        users.clear()
        users.addAll(usersManager.users.map { it.copy() })
    }

    override val root: Parent = vbox {
        tableview(users) {
            isEditable = true
            readonlyColumn("Имя", UserProfile::name) {
                minWidth = 300.0
            }
            column("Блокировка", UserProfile::isBlocked) {
                minWidth = 400.0
                makeEditable()
            }
            column("Ограничение на пароль", UserProfile::isOnPasswordRestrictions) {
                minWidth = 600.0
                makeEditable()
            }
        }

        hbox {
            button("Ок") {
                action(this@UserListView::handleUserListChanged)
                hboxConstraints {
                    marginTop = 50.0
                    marginLeft = 475.0
                    marginBottom = 30.0
                }
            }
            button("Отмена") {
                action(this@UserListView::handleClose)
                hboxConstraints {
                    marginTop = 50.0
                    marginLeft = 100.0
                    marginBottom = 30.0
                }
            }
        }
    }

    private fun handleUserListChanged() {
        usersManager.clearAndAddAll(users)
        close()
    }

    private fun handleClose() {
        if (isUserListChanged()) {
            openModalView<ChangesWillNotBeSavedPopup>()?.setOnHidden {
                Platform.runLater {
                    if(isNeedToBeSaved) {
                        usersManager.clearAndAddAll(users)
                    }
                }
            }
        }
        close()
    }

    private fun isUserListChanged(): Boolean {
        val initialList = usersManager.users
        if (users.size != initialList.size) {
            throw IllegalStateException()
        }
        for (index in users.indices) {
            if (users[index] != initialList[index]) {
                return true
            }
        }
        return false
    }
}

class ChangesWillNotBeSavedPopup : Fragment() {

    override val root: Parent = form {
        fieldset {
            field {
                label("Применить изменения?")
            }
            field {
                hbox {
                    button("Да") {
                        action {
                            find<UserListView> { isNeedToBeSaved = true }
                            this@ChangesWillNotBeSavedPopup.close()
                        }
                        hboxConstraints {
                            marginTop = 30.0
                            marginLeft = 100.0
                        }
                    }
                    button("Нет") {
                        action {
                            find<UserListView> { isNeedToBeSaved = false }
                            this@ChangesWillNotBeSavedPopup.close()
                        }
                        hboxConstraints {
                            marginTop = 30.0
                            marginLeft = 50.0
                        }
                    }
                }
            }
        }
    }
}