package aoc2015

import Input

object Day11 {

    fun part1(input: Input) {
        val pass = Password(input.asText())
        println(pass.next().value)
    }

    fun part2(input: Input) {
        val pass = Password(input.asText())
        println(pass.next().next().value)
    }

    private class Password(val value: String) {

        fun isValid(): Boolean {
            var hasStraight = false
            for (i in 0..(value.lastIndex - 2)) {
                if (value[i + 1] - value[i] == 1 && value[i + 2] - value[i + 1] == 1) {
                    hasStraight = true
                    break
                }
            }
            if (!hasStraight) {
                return false
            }

            if (value.contains('i') || value.contains('o') || value.contains('l')) {
                return false
            }

            var pairCount = 0
            for (ch in 'a'..'z') {
                val pair = "$ch$ch"
                if (value.contains(pair)) {
                    pairCount++
                    if (pairCount == 2) {
                        return true
                    }
                }
            }
            return false
        }

        fun increment(): Password {
            val chars = value.toCharArray()
            var index = chars.lastIndex
            while (index >= 0) {
                chars[index]++
                if (chars[index] > 'z') {
                    chars[index] = 'a'
                    index--
                } else {
                    break
                }
            }
            return Password(String(chars))
        }

        fun next(): Password {
            var pass = increment()
            while (!pass.isValid()) {
                pass = pass.increment()
            }
            return pass
        }

    }

}