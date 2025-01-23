package aoc2016

import AocDay2
import Input
import java.math.BigInteger
import java.security.MessageDigest


class Day5 : AocDay2 {

    private var doorIdPrefix: String = ""

    private val md = MessageDigest.getInstance("MD5")
    private val digest = ByteArray(16)

    override fun readInput(input: Input) {
        doorIdPrefix = input.asText().trim()
    }

    override fun part1() {
        var i = 0
        var password = ""

        while (password.length < 8) {
            val md5 = md5("$doorIdPrefix$i")
            if (md5.startsWith("00000")) {
                password += md5[5]
            }
            i++
        }
        println(password)
    }

    override fun part2() {
        var i = 0
        val password = CharArray(8) { ' '}
        var count = 0

        while (count < 8) {
            val md5 = md5("$doorIdPrefix$i")
            if (md5.startsWith("00000")) {
                val position = md5[5].code - '0'.code
                val value = md5[6]
                if (position in password.indices && password[position] == ' ') {
                    password[position] = value
                    count++
                }
            }
            i++
        }
        println(password.joinToString(""))
    }

    private fun md5(doorId: String): String {
        md.update(doorId.toByteArray())
        md.digest(digest, 0, digest.size)
        return String.format("%032x", BigInteger(1, digest))
    }
}