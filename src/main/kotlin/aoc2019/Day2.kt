package aoc2019

import AocDay
import Input

object Day2 : AocDay {

    override fun part1(input: Input) {
        val program = readProgram(input)
        program[1] = 12
        program[2] = 2
        IntCodeComputer(program).runAll()
        assert(program[0] == 5290681)
        println(program[0])
    }

    override fun part2(input: Input) {
        val program = readProgram(input)
        for (noun in 0..99) {
            for (verb in 0..99) {
                val copy = program.copyOf()
                copy[1] = noun
                copy[2] = verb
                IntCodeComputer(copy).runAll()
                if (copy[0] == 19690720) {
                    val result = 100 * noun + verb
                    assert(result == 5741)
                    println(result)
                    return
                }
            }
        }
    }

    private fun readProgram(input: Input): IntArray =
        input.asText().trim().split(",").map { it.trim().toInt() }.toIntArray()

}