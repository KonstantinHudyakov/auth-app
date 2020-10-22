package me.khudyakov.authapp.crypt

import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object TripleDes {
    const val algorithmName = "DESede"
    const val fullAlgorithmName = "DESede/ECB/PKCS5Padding"
    lateinit var password: String

    fun encrypt(bytes: ByteArray): ByteArray {
        return doCipher(bytes, Cipher.ENCRYPT_MODE)
    }

    fun decrypt(bytes: ByteArray): ByteArray {
        return doCipher(bytes, Cipher.DECRYPT_MODE)
    }

    private fun doCipher(input: ByteArray, mode: Int): ByteArray {
        val key: SecretKey = createKey(password)
        val cipher = Cipher.getInstance(fullAlgorithmName)
        cipher.init(mode, key)
        return cipher.doFinal(input)
    }

    private fun createKey(password: String): SecretKey {
        val digest = MessageDigest.getInstance("SHA-256")
        val passDigest = digest.digest(password.toByteArray(Charsets.UTF_8))
        val key = Arrays.copyOf(passDigest, 24)
        for(i in 0..7) {
            key[i + 16] = key[i]
        }
        return SecretKeySpec(key, algorithmName)
    }
}