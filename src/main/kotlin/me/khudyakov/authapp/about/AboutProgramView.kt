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
                label("Метод шифрования: 3DES")
            }
            field {
                label("Режим: Электронная кодовая книга")
            }
            field {
                label("Алгоритм хэширования: SHA")
            }
            field {
                label("Информация обо всех пользователях хранится в файле users.txt")
            }
            field {
                label("Реализация алгоритма шифрования находится в файле src/main/kotlin/me/khudyakov/authapp/crypt/TripleDes.kt")
            }
            field {
                label("В данном файле находится совокупность методов, реализующая кодирование и декодирование.")
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