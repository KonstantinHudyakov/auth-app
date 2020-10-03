package me.khudyakov.authapp

import me.khudyakov.authapp.entity.UserProfile
import java.io.*
import java.nio.charset.StandardCharsets

class UserStorage {
    companion object {
        const val fileName: String = "users.txt"
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
        users.add(UserProfile("ADMIN"))
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
        val writer = PrintWriter(OutputStreamWriter(FileOutputStream(file), StandardCharsets.UTF_8))
        writer.println(users.size)
        for (user in users) {
            writer.printUser(user)
        }
        writer.close()
    }

    private fun PrintWriter.printUser(user: UserProfile) {
        println("${user.name} ${user.password} ${user.isBlocked} ${user.isOnPasswordRestrictions}")
    }

    private fun load() {
        try {
            if (file.length() == 0L) {
                initializeUsersList()
            } else {
                val reader = BufferedReader(InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8))
                val size = reader.readLine().toInt()
                for (i in 0 until size) {
                    users.add(reader.readUser())
                }
                reader.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun BufferedReader.readUser(): UserProfile {
        val line = readLine()
        val properties = line.split(Regex(" +"))
        if (properties.size == 3) {
            return UserProfile(
                name = properties[0],
                password = "",
                isBlocked = properties[1].toBoolean(),
                isOnPasswordRestrictions = properties[2].toBoolean()
            )
        } else if (properties.size == 4) {
            return UserProfile(
                name = properties[0],
                password = properties[1],
                isBlocked = properties[2].toBoolean(),
                isOnPasswordRestrictions = properties[3].toBoolean()
            )
        }
        throw IllegalStateException()
    }
}