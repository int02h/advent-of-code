package aoc2019

import AocDay
import Input

object Day17 : AocDay {

    override fun part1(input: Input) {
        val programOutput = mutableListOf<Long>()
        val computer = IntCodeComputer(
            program = readProgram(input),
            input = mutableListOf(),
            output = programOutput
        )
        computer.runAll()

        val image = renderImage(programOutput)
        image.forEach(::println)

        var result = 0
        for (row in 1 until image.lastIndex) {
            for (col in 1 until image[0].lastIndex) {
                if (
                    image[row - 1][col] == '#' &&
                    image[row + 1][col] == '#' &&
                    image[row][col - 1] == '#' &&
                    image[row][col + 1] == '#'
                ) {
                    result += row * col
                }
            }
        }
        println(result)
    }

    override fun part2(input: Input) {
        println("Part 2")
        val programOutput = mutableListOf<Long>()
        val computer = IntCodeComputer(
            program = readProgram(input).also { it[0] = 2L },
            input = compileCommands(
                /* MAIN */ "A,B,A,C,A,B,C,B,C,B",
                /*    A */ "L,10,R,8,L,6,R,6",
                /*    B */ "L,8,L,8,R,8",
                /*    C */ "R,8,L,6,L,10,L,10",
                "n"
            ),
            output = programOutput
        )
        computer.runAll()
        println(programOutput.last())
    }

    private fun compileCommands(vararg commands: String): MutableList<Long> {
        val result = mutableListOf<Long>()
        commands.forEach { cmd ->
            cmd.forEach { ch -> result += ch.code.toLong() }
            result += 10L
        }
        return result
    }

    private fun renderImage(programOutput: List<Long>): List<String> =
        programOutput.map { it.toInt().toChar() }
            .joinToString(separator = "")
            .split("\n")
            .filter { it.isNotEmpty() }

    private fun readProgram(input: Input): LongArray =
        input.asText().trim().split(",").map { it.trim().toLong() }.toLongArray()
}