package aoc2019

import AocDay2
import Input

class Day21 : AocDay2 {

    private lateinit var program: LongArray

    override fun readInput(input: Input) {
        program = input.asText().trim().split(",").map { it.trim().toLong() }.toLongArray()
    }

    override fun part1() {
        runScript(
            "NOT A T",
            "NOT C J",
            "AND D J",
            "OR T J",
            "WALK"
        )
    }

    override fun part2() {
        runScript(
            "NOT A J",
            "NOT B T",
            "OR T J",
            "NOT C T",
            "OR T J",
            "AND D J",
            "AND H J",
            "NOT A T",
            "OR T J",
            "RUN"
        )
    }

    private fun runScript(vararg script: String) {
        val input = script.joinToString("\n", postfix = "\n").map { it.code.toLong() }
        val output = AsciiOutput()
        val computer = IntCodeComputer(program, input.toMutableList(), output)
        computer.runAll()
    }

    private class AsciiOutput : ArrayList<Long>() {
        override fun add(element: Long): Boolean {
            if (element > 255) {
                println(element)
            }
            return true
        }
    }

}