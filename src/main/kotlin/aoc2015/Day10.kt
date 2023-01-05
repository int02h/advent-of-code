package aoc2015

import Input

object Day10 {

    fun part1(input: Input) {
        println(play(input, 40))
    }

    fun part2(input: Input) {
        println(play(input, 50))
    }

    private fun play(input: Input, roundCount: Int): Int {
        var str = input.asText()
        val result = StringBuilder()
        repeat(roundCount) {
            result.clear()
            var index = 0
            while (index < str.length) {
                val ch = str[index]
                var count = 0
                while (str.getOrNull(index) == ch) {
                    index++
                    count++
                }
                result.append(count).append(ch)
            }
            str = result.toString()
        }
        return result.length
    }

}