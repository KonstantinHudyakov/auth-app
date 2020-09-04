package me.khudyakov.authapp.main

import javafx.scene.control.Menu
import javafx.stage.StageStyle
import me.khudyakov.authapp.PasswordDoesntMeetTheRequirementsPopup
import me.khudyakov.authapp.adduser.AddUserView
import me.khudyakov.authapp.openModalView
import me.khudyakov.authapp.userlist.UserListView
import tornadofx.action
import tornadofx.item

class AdminMainView : UserMainView() {

    override fun Menu.addActions() {
        addChangePasswordMenuItem()
        item("Просмотреть список пользователей").action(this@AdminMainView::showUserListView)
        item("Добавить пользователя").action(this@AdminMainView::showAddUserView)
    }

    private fun showUserListView() {
        openModalView<UserListView>()
    }

    private fun showAddUserView() {
        openModalView<AddUserView>()
    }
}