package aoc2016

import AocDay2
import Input
import util.findAllNumbers

class Day8 : AocDay2 {

    private lateinit var commands: List<Command>

    override fun readInput(input: Input) {
        commands = input.asLines().map { line ->
            val values = line.findAllNumbers()
            when {
                line.startsWith("rect") -> Command.Rect(values[0], values[1])
                line.startsWith("rotate column") -> Command.RotateColumn(values[0], values[1])
                line.startsWith("rotate row") -> Command.RotateRow(values[0], values[1])
                else -> error(line)
            }
        }
    }

    override fun part1() {
        val screen = Array(6) { BooleanArray(50) }
        commands.forEach {
            it.execute(screen)
        }
        println(screen.sumOf { row -> row.count { it } })
    }

    override fun part2() {
        val screen = Array(6) { BooleanArray(50) }
        commands.forEach {
            it.execute(screen)
        }
        screen.forEach { row ->
            row.forEach { print(if (it) '#' else '.') }
            println()
        }
    }

    private sealed interface Command {

        fun execute(screen: Array<BooleanArray>)

        class Rect(val width: Int, val height: Int) : Command {
            override fun execute(screen: Array<BooleanArray>) {
                for (row in 0 until height) {
                    for (col in 0 until width) {
                        screen[row][col] = true
                    }
                }
            }
        }

        class RotateColumn(val column: Int, val shift: Int) : Command {
            override fun execute(screen: Array<BooleanArray>) {
                val columnValues = mutableListOf<Boolean>()
                screen.forEach { row -> columnValues += row[column] }
                columnValues.forEachIndexed { index, value ->
                    screen[(index + shift) % screen.size][column] = value
                }
            }
        }

        class RotateRow(val row: Int, val shift: Int) : Command {
            override fun execute(screen: Array<BooleanArray>) {
                val rowValues = screen[row].toMutableList()
                rowValues.forEachIndexed { index, value ->
                    screen[row][(index + shift) % screen[0].size] = value
                }
            }
        }
    }
}