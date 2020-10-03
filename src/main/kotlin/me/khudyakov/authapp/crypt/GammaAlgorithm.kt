package me.khudyakov.authapp.crypt

import kotlin.random.Random

object GammaAlgorithm : CryptoAlgorithm {

    override fun encode(text: String): String {
        val key = createKey(text)
        val encoded = convert(text, key)
        return encoded + key.toChar()
    }

    override fun decode(code: String): String {
        if(code.isEmpty()) {
            return ""
        }
        val key = code[code.length - 1].toInt()
        return convert(code.substring(0, code.length - 1), key)
    }

    private fun convert(text: String, key: Int): String {
        val numbers = text.toIntList()
        val converted = mutableListOf<Int>()
        for (num in numbers) {
            converted.add(num xor key)
        }
        return converted.map(Int::toChar).joinToString(separator = "")
    }

    private fun createKey(text: String): Int {
        val seed = createSeed(text)
        val random = Random(seed)
        return random.nextInt(256)
    }

    private fun createSeed(text: String): Int {
        var seed = 0
        var ind = 0
        while (ind < text.length) {
            seed = (seed + text[ind].toInt()) % 256
            ind += 2
        }
        return seed
    }

    private fun String.toIntList(): List<Int> {
        val list = mutableListOf<Int>()
        for (ch in this) {
            list.add(ch.toInt())
        }
        return list
    }
}