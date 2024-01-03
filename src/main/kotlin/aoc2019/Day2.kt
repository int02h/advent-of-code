package aoc2019

import AocDay
import Input

object Day2 : AocDay {

    override fun part1(input: Input) {
        val program = readProgram(input)
        program[1] = 12
        program[2] = 2
        val computer = IntCodeComputer(program)
        computer.runAll()
        assert(computer.memory[0] == 5290681L)
        println(computer.memory[0])
    }

    override fun part2(input: Input) {
        val program = readProgram(input)
        for (noun in 0..99) {
            for (verb in 0..99) {
                program[1] = noun.toLong()
                program[2] = verb.toLong()
                val computer = IntCodeComputer(program)
                computer.runAll()
                if (computer.memory[0] == 19690720L) {
                    val result = 100 * noun + verb
                    assert(result == 5741)
                    println(result)
                    return
                }
            }
        }
    }

    private fun readProgram(input: Input): LongArray =
        input.asText().trim().split(",").map { it.trim().toLong() }.toLongArray()

}