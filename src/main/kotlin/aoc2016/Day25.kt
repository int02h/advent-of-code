package aoc2016

import AocDay2
import Input

class Day25 : AocDay2 {

    private var ip = 0
    private val registers = mutableMapOf<String, Long>().withDefault { 0L }
    private lateinit var instructions: List<String>
    private val output = mutableListOf<Long>()

    override fun readInput(input: Input) {
        instructions = input.asLines()
    }

    override fun part1() {
        var a = 0L
        while (true) {
            registers["a"] = a
            run()
            if (checkOutput()) break
            reset()
            a++
        }
        println(a)
    }

    override fun part2() {
    }

    private fun reset() {
        ip = 0
        registers.clear()
        output.clear()
    }

    private fun checkOutput(): Boolean {
        for (i in output.indices) {
            if (i % 2 != output[i].toInt()) return false
        }
        return true
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
                "out" -> {
                    output += values[1].toLongOrNull() ?: registers.getValue(values[1])
                    if (output.size == 50) break
                    ip++
                }
            }
        }
    }

}

