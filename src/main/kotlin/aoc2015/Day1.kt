package aoc2015

import Input

object Day1 {

    fun part1(input: Input) {
        val text = input.asText()
        val countUp = text.count { it == '(' }
        val countDown = text.length - countUp
        println(countUp - countDown)
    }

    fun part2(input: Input) {
        var floor = 0
        input.asText().forEachIndexed { index, ch ->
            when (ch) {
                '(' -> floor++
                ')' -> floor--
            }
            if (floor == -1) {
                println(index + 1)
                return
            }
        }

    }
}