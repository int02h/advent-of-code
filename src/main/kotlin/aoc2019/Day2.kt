package aoc2019

import AocDay
import Input

object Day2 : AocDay {

    override fun part1(input: Input) {
        val program = readProgram(input)
        program[1] = 12
        program[2] = 2
        IntCodeComputer(program).runAll()
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
                    print(100 * noun + verb)
                    return
                }
            }
        }
    }

    private fun readProgram(input: Input): IntArray =
        input.asText().trim().split(",").map { it.trim().toInt() }.toIntArray()

    class IntCodeComputer(private val program: IntArray) {
        var position = 0

        fun runAll() {
            while (position != -1) {
                executeNext()
            }
        }

        private fun executeNext() {
            when (val opcode = program[position]) {
                1 -> {
                    val res = getIndirectValue(1) + getIndirectValue(2)
                    setIndirectValue(3, res)
                    position += 4
                }
                2 -> {
                    val res = getIndirectValue(1) * getIndirectValue(2)
                    setIndirectValue(3, res)
                    position += 4
                }
                99 -> position = -1
                else -> error("Unknown opcode: $opcode")
            }
        }

        private fun getIndirectValue(offset: Int): Int {
            return program[program[position + offset]]
        }

        private fun setIndirectValue(offset: Int, value: Int) {
            program[program[position + offset]] = value
        }
    }

}