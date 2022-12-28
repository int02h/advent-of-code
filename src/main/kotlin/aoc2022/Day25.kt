package aoc2022

import Input

object Day25 {

    fun part1(input: Input) {
        val sum = input.asLines().sumOf { snafuToDecimal(it) }
        println(decimalToSnafu(sum))
    }

    fun part2(input: Input) {

    }

    private fun snafuToDecimal(value: String): Long {
        var result = 0L
        var power5 = 1L
        for (i in value.lastIndex downTo 0) {
            result += when (value[i]) {
                '0', '1', '2' -> value[i].digitToInt() * power5
                '-' -> -power5
                '=' -> -2 * power5
                else -> error(value[i])
            }
            power5 *= 5
        }
        return result
    }

    private fun decimalToSnafu(decimalValue: Long): String {
        var value = decimalValue
        var power5 = 5L
        val digits = mutableListOf<Int>()
        digits += (value % power5).toInt()
        value -= value % power5

        while (value != 0L) {
            val r = (value % (power5 * 5)) / power5
            digits += r.toInt()
            value -= r * power5
            power5 *= 5
        }

        digits.add(0)

        for (i in 0..digits.lastIndex) {
            if (digits[i] >= 3) {
                digits[i] = digits[i] - 5
                digits[i + 1] += 1
            }
        }

        if (digits.last() == 0) {
            digits.removeLast()
        }

        var snafu = ""
        digits.forEach { d ->
            when (d) {
                in 0..2 -> snafu = d.toString() + snafu
                -1 -> snafu = "-$snafu"
                -2 -> snafu = "=$snafu"
            }
        }

        return snafu
    }
}