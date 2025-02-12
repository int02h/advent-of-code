package aoc2016

import AocDay2
import Input

class Day23 : AocDay2 {

    private var ip = 0
    private val registers = Registers()
    private lateinit var instructions: MutableList<String>

    override fun readInput(input: Input) {
        instructions = input.asLines().toMutableList()
    }

    override fun part1() {
        registers.a = 7
        run()
        println(registers.a)
    }

    override fun part2() {
        instructions[2] = "mul a b"
        for (i in 3..9) {
            instructions[i] = "nop"
        }
        registers.a = 12L
        run()
        println(registers.a)
    }

    private fun run() {
        while (ip < instructions.size) {
            val values = instructions[ip].split(' ')
            when (values[0]) {
                "cpy" -> {
                    try {
                        registers[values[2]] = values[1].toLongOrNull() ?: registers[values[1]]
                    } catch (e: InvalidInstruction) {
                        // nothing
                        println(e)
                    }
                    ip++
                }
                "inc" -> {
                    try {
                        registers[values[1]] = registers[values[1]] + 1
                    } catch (e: InvalidInstruction) {
                        // nothing
                    }
                    ip++
                }
                "dec" -> {
                    try {
                        registers[values[1]] = registers[values[1]] - 1
                    } catch (e: InvalidInstruction) {
                        // nothing
                    }
                    ip++
                }
                "jnz" -> {
                    val value = try {
                        values[1].toLongOrNull() ?: registers[values[1]]
                    } catch (e: InvalidInstruction) {
                        0
                    }
                    ip += if (value != 0L) (values[2].toIntOrNull() ?: registers[values[2]].toInt()) else 1
                }
                "tgl" -> {
                    val value = values[1].toIntOrNull() ?: registers[values[1]].toInt()
                    val index = ip + value
                    if (index in instructions.indices) {
                        instructions[index] = toggle(instructions[index])
                    }
                    ip++
                }
                "mul" -> {
                    registers[values[1]] = registers[values[1]] * registers[values[2]]
                    ip++
                }
                "nop" -> ip++
            }
        }
    }

    private fun toggle(instruction: String): String {
        val values = instruction.split(' ')
        if (values.size == 2) {
            return if (values[0] == "inc") {
                "dec ${values[1]}"
            } else {
                "inc ${values[1]}"
            }
        }
        return if (values[0] == "jnz") {
            "cpy ${values[1]} ${values[2]}"
        } else {
            "jnz ${values[1]} ${values[2]}"
        }
    }

    private class Registers {

        var a: Long = 0
        var b: Long = 0
        var c: Long = 0
        var d: Long = 0

        operator fun get(name: String): Long = when (name) {
            "a" -> a
            "b" -> b
            "c" -> c
            "d" -> d
            else -> throw InvalidInstruction()
        }

        operator fun set(name: String, value: Long) {
            when (name) {
                "a" -> a = value
                "b" -> b = value
                "c" -> c = value
                "d" -> d = value
                else -> throw InvalidInstruction()
            }
        }
    }

    private class InvalidInstruction : Exception()

}

