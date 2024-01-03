package aoc2019

import AocDay
import Input

object Day9 : AocDay {

    override fun part1(input: Input) {
        runWithId(input, 1)
    }

    override fun part2(input: Input) {
        runWithId(input, 2)
    }

    private fun runWithId(input: Input, id: Int) {
        val program = readProgram(input)
        val output = mutableListOf<Long>()
        IntCodeComputer(
            program = program,
            input = mutableListOf(id.toLong()),
            output = output
        ).runAll()
        println(output)
    }

    private fun readProgram(input: Input): LongArray =
        input.asText().trim().split(",").map { it.trim().toLong() }.toLongArray()
}