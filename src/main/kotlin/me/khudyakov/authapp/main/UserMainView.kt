package me.khudyakov.authapp.main

import javafx.scene.Parent
import javafx.scene.control.Menu
import javafx.stage.StageStyle
import me.khudyakov.authapp.PasswordDoesntMeetTheRequirementsPopup
import me.khudyakov.authapp.about.AboutProgramView
import me.khudyakov.authapp.openModalView
import me.khudyakov.authapp.pass.ChangePasswordView
import tornadofx.*

open class UserMainView : View() {

    override val root: Parent = borderpane {
        top = menubar {
            menu("Действия") {
                addActions()
                item("Выход").action { this@UserMainView.close() }
            }
            menu("Справка") {
                item("О программе").action(this@UserMainView::showAboutProgramView)
            }
        }
    }

    protected open fun Menu.addActions() {
        addChangePasswordMenuItem()
    }

    protected fun Menu.addChangePasswordMenuItem() {
        item("Сменить пароль").action(this@UserMainView::showChangePasswordView)
    }

    private fun showChangePasswordView() {
        openModalView<ChangePasswordView>()
    }

    private fun showAboutProgramView() {
        openModalView<AboutProgramView>()
    }
}