package aoc2023

import AocDay
import Input
import java.util.*
import kotlin.math.max

object Day16 : AocDay {

    override fun part1(input: Input) {
        val layout = input.asLines().map { line -> line.map { Cell(it) } }
        println(countEnergized(layout, Beam(0, 0, Direction.RIGHT)))
    }

    override fun part2(input: Input) {
        val layout = input.asLines().map { line -> line.map { Cell(it) } }
        var best = 0
        for (row in 0..layout.lastIndex) {
            best = max(best, countEnergized(layout, Beam(row, 0, Direction.RIGHT)))
            best = max(best, countEnergized(layout, Beam(row, layout[0].lastIndex, Direction.LEFT)))
        }
        for (col in 0..layout[0].lastIndex) {
            best = max(best, countEnergized(layout, Beam(0, col, Direction.DOWN)))
            best = max(best, countEnergized(layout, Beam(layout.lastIndex, col, Direction.UP)))
        }
        println(best)
    }

    private fun countEnergized(layout: List<List<Cell>>, startBeam: Beam): Int {
        val stack = Stack<Beam>()
        stack.push(startBeam)
        layout.forEach { line -> line.forEach { cell -> cell.reset() } }

        while (stack.isNotEmpty()) {
            val b = stack.pop()
            if (b.row < 0 || b.col < 0 || b.row >= layout.size || b.col >= layout[0].size) {
                continue
            }
            if (
                layout[b.row][b.col].energized &&
                layout[b.row][b.col].energizeDirection == b.direction
            ) {
                continue
            }
            layout[b.row][b.col].handle(b).forEach(stack::push)
        }

        return layout.sumOf { line -> line.count { cell -> cell.energized } }
    }

    private class Cell(val value: Char) {
        var energized = false
        var energizeDirection: Direction? = null

        fun handle(beam: Beam): List<Beam> {
            energized = true
            energizeDirection = beam.direction
            return when (value) {
                '.' -> listOf(beam.move())
                '/' -> {
                    val direction = when (beam.direction) {
                        Direction.UP -> Direction.RIGHT
                        Direction.DOWN -> Direction.LEFT
                        Direction.LEFT -> Direction.DOWN
                        Direction.RIGHT -> Direction.UP
                    }
                    listOf(beam.direction(direction).move())
                }
                '\\' -> {
                    val direction = when (beam.direction) {
                        Direction.UP -> Direction.LEFT
                        Direction.DOWN -> Direction.RIGHT
                        Direction.LEFT -> Direction.UP
                        Direction.RIGHT -> Direction.DOWN
                    }
                    listOf(beam.direction(direction).move())
                }
                '-' -> {
                    when (beam.direction) {
                        Direction.UP,
                        Direction.DOWN -> {
                            val beam2 = Beam(beam.row, beam.col, Direction.LEFT)
                            listOf(beam.direction(Direction.RIGHT).move(), beam2.move())
                        }
                        Direction.LEFT,
                        Direction.RIGHT -> listOf(beam.move())
                    }
                }
                '|' -> {
                    when (beam.direction) {
                        Direction.UP,
                        Direction.DOWN -> listOf(beam.move())
                        Direction.LEFT,
                        Direction.RIGHT -> {
                            val beam2 = Beam(beam.row, beam.col, Direction.DOWN)
                            listOf(beam.direction(Direction.UP).move(), beam2.move())
                        }
                    }
                }
                else -> error("Impossible")
            }
        }

        fun reset() {
            energized = false
            energizeDirection = null
        }
    }

    private class Beam(val row: Int, val col: Int, val direction: Direction) {

        fun direction(direction: Direction): Beam = Beam(row, col, direction)

        fun move(): Beam {
            return when (direction) {
                Direction.UP -> Beam(row - 1, col, direction)
                Direction.DOWN -> Beam(row + 1, col, direction)
                Direction.LEFT -> Beam(row, col - 1, direction)
                Direction.RIGHT -> Beam(row, col + 1, direction)
            }
        }
    }

    private enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}