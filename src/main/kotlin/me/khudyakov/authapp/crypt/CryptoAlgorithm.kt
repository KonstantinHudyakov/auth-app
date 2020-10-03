package me.khudyakov.authapp.crypt

interface CryptoAlgorithm {
    fun encode(text: String): String

    fun decode(code: String): String
}