package aoc2016

import AocDay2
import Input

class Day12 : AocDay2 {

    private var ip = 0
    private val registers = mutableMapOf<String, Long>().withDefault { 0L }
    private lateinit var instructions: List<String>

    override fun readInput(input: Input) {
        instructions = input.asLines()
    }

    override fun part1() {
        run()
        println(registers["a"])
    }

    override fun part2() {
        registers["c"] = 1
        run()
        println(registers["a"])
    }

    private fun run() {
        while (ip < instructions.size) {
            val values = instructions[ip].split(' ')
            when (values[0]) {
                "cpy" -> {
                    registers[values[2]] = values[1].toLongOrNull() ?: registers.getValue(values[1])
                    ip++
                }
                "inc" -> {
                    registers[values[1]] = registers.getValue(values[1]) + 1
                    ip++
                }
                "dec" -> {
                    registers[values[1]] = registers.getValue(values[1]) - 1
                    ip++
                }
                "jnz" -> {
                    val value = values[1].toLongOrNull() ?: registers.getValue(values[1])
                    ip += if (value != 0L) values[2].toInt() else 1
                }
            }
        }
    }

}

