package me.khudyakov.authapp.about

import javafx.scene.Parent
import tornadofx.*

class AboutProgramView : View() {

    override val root: Parent = form {
        fieldset {
            field {
                label("Программа разграничения полномочий пользователей на основе парольной аутентификации.")
            }
            field {
                label("Разработчик: Худяков Константин А-13-17")
            }
            field {
                label("Вариант №42. Ограничение на выбираемый пароль:")
            }
            field {
                label("Чередование символов кириллицы, латинских букв, цифр и снова символов кириллицы.")
            }
            field {
                borderpane {
                    center = button("Ок") {
                        action(this@AboutProgramView::close)
                    }
                }
            }
        }
    }
}