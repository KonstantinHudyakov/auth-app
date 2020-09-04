package me.khudyakov.authapp

import me.khudyakov.authapp.entity.UserProfile
import java.io.*

class UserStorage {
    companion object {
        const val fileName: String = "users.bin"
    }

    private val file = File(fileName)
    val users: MutableList<UserProfile> = mutableListOf()

    init {
        if (!file.exists()) {
            initializeUsersList()
        } else {
            load()
        }
    }

    private fun initializeUsersList() {
        file.createNewFile()
        users.add(
            UserProfile(
                "ADMIN",
                isOnPasswordRestrictions = false
            )
        ) // todo: change isOnPasswordRestrictions to true
        save()
    }

    fun initWith(users: List<UserProfile>) {
        this.users.apply {
            clear()
            addAll(users)
        }
        currentUser = users.find { it.name == "ADMIN" }
            ?: throw IllegalArgumentException()
        save()
    }

    fun save() {
        val outputStream = ObjectOutputStream(FileOutputStream(file))
        outputStream.writeInt(users.size)
        for (user in users) {
            outputStream.writeObject(user)
        }
        outputStream.close()
    }

    private fun load() {
        try {
            if (file.length() == 0L) {
                initializeUsersList()
            } else {
                val inputStream = ObjectInputStream(FileInputStream(file))
                users.clear()
                val size = inputStream.readInt()
                for (i in 0 until size) {
                    val user = inputStream.readObject()
                    users.add(user as UserProfile)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}