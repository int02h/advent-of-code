package aoc2015

import Input

object Day5 {

    private val VOWELS = "aeiou".toSet()

    fun part1(input: Input) {
        println(input.asLines().count { it.isNice() })
    }

    fun part2(input: Input) {
        println(input.asLines().count { it.isNice2() })
    }

    private fun String.isNice(): Boolean {
        if (contains("ab") || contains("cd") || contains("pq") || contains("xy")) {
            return false
        }
        if (count { VOWELS.contains(it) } < 3) {
            return false
        }
        for (i in 1..lastIndex) {
            if (this[i] == this[i - 1]) {
                return true
            }
        }
        return false
    }

    private fun String.isNice2(): Boolean {
        var i = 1
        var hasDoubles = false
        while (i <= lastIndex) {
            val pair = "${this[i - 1]}${this[i]}"
            val otherIndex = indexOf(pair, i + 1)
            if (otherIndex > 0) {
                hasDoubles = true
                break
            }
            i++
        }
        if (!hasDoubles) {
            return false
        }

        i = 2
        while (i <= lastIndex) {
            if (this[i] == this[i - 2]) {
                return true
            } else {
                i++
            }
        }
        return false
    }

}