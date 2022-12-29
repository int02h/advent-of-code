package aoc2015

import Input
import java.math.BigInteger
import java.security.MessageDigest

object Day4 {

    fun part1(input: Input) {
        val secret = input.asText()
        var number = -1
        do {
            number++
            val md5 = md5(secret + number)
        } while (!md5.startsWith("00000"))
        println(number)
    }

    fun part2(input: Input) {
        val secret = input.asText()
        var number = -1
        do {
            number++
            val md5 = md5(secret + number)
        } while (!md5.startsWith("000000"))
        println(number)
    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}