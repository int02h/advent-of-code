package aoc2017

import AocDay2
import Input
import util.findAllNumbers

class Day2 : AocDay2 {

    private lateinit var values: List<List<Int>>

    override fun readInput(input: Input) {
        values = input.asLines().map { line -> line.findAllNumbers() }
    }

    override fun part1() {
        println(values.sumOf { line -> line.max() - line.min() })
    }

    override fun part2() {
        println(values.sumOf(::findDividePair))
    }

    private fun findDividePair(line: List<Int>): Int {
        for (i in line.indices) {
            for (j in (i + 1) until line.size) {
                if (line[i] % line[j] == 0) {
                    return line[i] / line[j]
                }
                if (line[j] % line[i] == 0) {
                    return line[j] / line[i]
                }
            }
        }
        error("Oops")
    }
}