package me.khudyakov.authapp

import javafx.scene.Parent
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*

inline fun <reified T : UIComponent> openModalView(): Stage? {
    val stage = find<T>().openModal(stageStyle = StageStyle.UTILITY)
    stage?.toFront()
    return stage
}

open class InformationPopup(private val text: String) : Fragment() {
    override val root: Parent = form {
        fieldset {
            field {
                label(this@InformationPopup.text)
            }
            field {
                borderpane {
                    center = button("Ок") {
                        action { this@InformationPopup.close() }
                        borderpaneConstraints {
                            marginTop = 30.0
                        }
                    }
                }
            }
        }
    }
}

class NameMustBeNotEmptyPopup : InformationPopup("Имя пользователя не может быть пустым.")
class PasswordMustBeNotEmptyPopup: InformationPopup("Пароль не может быть пустым.")
class UserNotFoundPopup : InformationPopup("Пользователь с таким именем не найден.")
class WrongPasswordPopup : InformationPopup("Введён неверный пароль.")
class WrongOldPasswordPopup : InformationPopup("Введён неверный предыдущий пароль.")
class PasswordMismatchPopup: InformationPopup("Пароли не совпадают.")
class PasswordDoesntMeetTheRequirementsPopup :
    InformationPopup("Пароль не соответствует требованию: чередование символов кириллицы, латинских букв, цифр и снова символов кириллицы.")
class NameIsTakenPopup : InformationPopup("Данное имя уже занято.")
class UserIsBlockedPopup : InformationPopup("Данный пользователь заблокирован")
class UserSuccessfullyAddedPopup: InformationPopup("Пользователь успешно добавлен!")
class AuthAttemptsLimitExceededPopup: InformationPopup("Превышено допустимое количество попыток авторизоваться.")