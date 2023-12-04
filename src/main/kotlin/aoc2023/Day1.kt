package aoc2023

import AocDay
import Input

object Day1 : AocDay {

    override fun part1(input: Input) {
        input.asLines()
            .map {
                val d1 = it.getDigit(0, 1)
                val d2 = it.getDigit(it.lastIndex, -1)
                "$d1$d2".toInt()
            }
            .sum()
            .apply(::println)
    }

    override fun part2(input: Input) {
        val digits = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val sum = input.asLines().sumOf {
            var firstDigit: Char? = null
            var line = it
            while (firstDigit == null) {
                if (line[0].isDigit()) {
                    firstDigit = line[0]
                    break
                }
                for (i in digits.indices) {
                    if (line.startsWith(digits[i])) {
                        firstDigit = (i + 1).digitToChar()
                        break
                    }
                }
                line = line.drop(1)
            }

            var lastDigit: Char? = null
            line = it
            while (lastDigit == null) {
                if (line.last().isDigit()) {
                    lastDigit = line.last()
                    break
                }
                for (i in digits.indices) {
                    if (line.endsWith(digits[i])) {
                        lastDigit = (i + 1).digitToChar()
                        break
                    }
                }
                line = line.dropLast(1)
            }
            "$firstDigit$lastDigit".toInt()
        }
        println(sum)
    }

    private fun String.getDigit(start: Int, delta: Int): Char {
        var i = start
        var result = get(i)
        while (!result.isDigit()) {
            i += delta
            result = get(i)
        }
        return result
    }
}