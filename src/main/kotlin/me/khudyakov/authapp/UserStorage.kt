package me.khudyakov.authapp

import me.khudyakov.authapp.crypt.TripleDes
import me.khudyakov.authapp.entity.UserProfile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

class UserStorage {
    companion object {
        const val fileName: String = "users.txt"
    }

    private val file = File(fileName)
    val users: MutableList<UserProfile> = mutableListOf()

    fun init() {
        if (!file.exists()) {
            initializeUsersList()
        } else {
            load()
        }
    }

    private fun initializeUsersList() {
        file.createNewFile()
        users.add(UserProfile("ADMIN", isOnPasswordRestrictions = false))
        save()
    }

    fun replaceAllWith(users: List<UserProfile>) {
        this.users.apply {
            clear()
            addAll(users)
        }
        currentUser = users.find { it.name == "ADMIN" }
            ?: throw IllegalArgumentException()
        save()
    }

    fun save() {
        val rawBytesOutput = ByteArrayOutputStream()
        val writer = PrintWriter(OutputStreamWriter(rawBytesOutput, StandardCharsets.UTF_8))
        writer.println(users.size)
        for (user in users) {
            writer.printUser(user)
        }
        writer.close()

        val encryptedBytes = TripleDes.encrypt(rawBytesOutput.toByteArray())
        file.writeBytes(encryptedBytes)
    }

    private fun PrintWriter.printUser(user: UserProfile) {
        println("${user.name} ${user.password} ${user.isBlocked} ${user.isOnPasswordRestrictions}")
    }

    private fun load() {
        val encryptedBytes = file.readBytes()
        val decryptedBytes = TripleDes.decrypt(encryptedBytes)
        val usersString = String(decryptedBytes)
        val parsedUsers = parseUsers(usersString)
        users.addAll(parsedUsers)
    }

    private fun parseUsers(input: String): List<UserProfile> {
        val users = mutableListOf<UserProfile>()
        val lines = input.lines()
        val size = lines[0].toInt()
        for (i in 0 until size) {
            users.add(parseOneUser(lines[i + 1]))
        }
        return users
    }

    private fun parseOneUser(line: String): UserProfile {
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