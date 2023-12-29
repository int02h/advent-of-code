package aoc2019

import AocDay
import Input

object Day5 : AocDay {
    override fun part1(input: Input) {
        val result = runWithId(input, id = 1)
        assert(result == 9961446)
        println(result)
    }

    override fun part2(input: Input) {
        val result = runWithId(input, id = 5)
        assert(result == 742621)
        println(result)
    }

    private fun runWithId(input: Input, id: Int): Int {
        val program = readProgram(input)
        val output = mutableListOf<Int>()
        IntCodeComputer(
            program = program,
            input = mutableListOf(id),
            output = output
        ).runAll()
        val nonZero = output.filter { it != 0 }
        assert(nonZero.size == 1)
        return nonZero.first()
    }

    private fun readProgram(input: Input): IntArray =
        input.asText().trim().split(",").map { it.trim().toInt() }.toIntArray()
}