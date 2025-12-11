package aoc2017

import AocDay2
import Input

class Day1 : AocDay2 {

    private lateinit var digits: String

    override fun readInput(input: Input) {
        digits = input.asText().trim()
    }

    override fun part1() {
        var sum = 0
        for (i in digits.indices) {
            val next = digits[(i + 1) % digits.length]
            if (digits[i] == next) {
                sum += digits[i].digitToInt()
            }
        }
        println(sum)
    }

    override fun part2() {
        var sum = 0
        for (i in digits.indices) {
            val next = digits[(i + digits.length / 2) % digits.length]
            if (digits[i] == next) {
                sum += digits[i].digitToInt()
            }
        }
        println(sum)
    }
}