package aoc2024

import AocDay2
import Input
import util.RowColPosition as Position

class Day15 : AocDay2 {

    private var robotRow: Int = -1
    private var robotCol: Int = -1
    private lateinit var map: Array<CharArray>
    private var commands: String = ""

    override fun readInput(input: Input) {
        var isMapReading = true
        val map = mutableListOf<CharArray>()
        input.asLines()
            .map { it.trim().toCharArray() }
            .forEachIndexed { row, line ->
                if (line.isEmpty()) {
                    isMapReading = false
                } else {
                    if (isMapReading) {
                        val col = line.indexOf('@')
                        if (col != -1) {
                            line[col] = '.'
                            robotRow = row
                            robotCol = col
                        }
                        map += line
                    } else {
                        commands += String(line)
                    }
                }
            }
        this.map = map.toTypedArray()
    }

    override fun part1() {
        commands.forEach { command -> moveRobot(command) }
        var sum = 0
        for (row in map.indices) {
            for (col in map[row].indices) {
                if (map[row][col] == 'O') {
                    sum += 100 * row + col
                }
            }
        }
        println(sum)
    }

    override fun part2() {
        map = Array(map.size) { row ->
            map[row].flatMap { ch ->
                when (ch) {
                    '#' -> listOf('#', '#')
                    'O' -> listOf('[', ']')
                    '.' -> listOf('.', '.')
                    else -> error(ch)
                }
            }.toCharArray()
        }
        robotCol *= 2

        commands.forEach { command ->
            val movement = move(robotRow, robotCol, command)
            when (movement) {
                Movement2.NoMovement -> Unit
                is Movement2.Robot -> {
                    robotRow += movement.dRow
                    robotCol += movement.dCol
                }
                is Movement2.RobotAndBoxes -> {
                    movement.boxes.forEach { b -> map[b.row][b.col] = '.' }
                    movement.boxes.forEach { b -> map[b.row + movement.dRow][b.col + movement.dCol] = b.value }
                    robotRow += movement.dRow
                    robotCol += movement.dCol
                }
            }
        }

        var sum = 0
        for (row in map.indices) {
            for (col in map[row].indices) {
                if (map[row][col] == '[') {
                    sum += 100 * row + col
                }
            }
        }
        println(sum)
    }

    private fun moveRobot(command: Char) {
        val movement = when (command) {
            '<' -> getBoxes(dCol = -1)
            '>' -> getBoxes(dCol = 1)
            '^' -> getBoxes(dRow = -1)
            'v' -> getBoxes(dRow = 1)
            else -> error("Bad command")
        }
        when (movement) {
            is Movement.RobotAndBoxes -> {
                movement.boxes.forEach { b -> map[b.row][b.col] = '.' }
                movement.boxes.forEach { b -> map[b.row + movement.dRow][b.col + movement.dCol] = 'O' }
                robotRow += movement.dRow
                robotCol += movement.dCol
            }
            is Movement.Robot -> {
                robotRow += movement.dRow
                robotCol += movement.dCol
            }
            Movement.NoMovement -> Unit
        }
    }

    private fun getBoxes(dRow: Int = 0, dCol: Int = 0): Movement {
        var r = robotRow + dRow
        var c = robotCol + dCol
        if (map[r][c] == '.') {
            return Movement.Robot(dRow, dCol)
        }
        if (map[r][c] == '#') {
            return Movement.NoMovement
        }

        val boxes = mutableListOf<Position>()
        while (map[r][c] == 'O') {
            boxes += Position(r, c)
            r += dRow
            c += dCol
        }
        return if (map[r][c] == '.') {
            Movement.RobotAndBoxes(boxes, dRow, dCol)
        } else {
            Movement.NoMovement
        }
    }

    private fun move(row: Int, col: Int, command: Char, boxes: Set<Box> = emptySet()): Movement2 {
        val isRobot = robotRow == row && robotCol == col
        return when (command) {
            '<' -> getBoxes2(robotRow, robotCol, dCol = -1)
            '>' -> getBoxes2(robotRow, robotCol, dCol = 1)
            '^', 'v' -> {
                val dRow = if (command == '^') -1 else 1
                if (isRobot) {
                    move(robotRow + dRow, robotCol, command)
                } else {
                    when (map[row][col]) {
                        '[' -> {
                            val m1 = move(row + dRow, col, command, boxes + Box(row, col, map[row][col]))
                            val m2 = move(row + dRow, col + 1, command, boxes + Box(row, col + 1, map[row][col + 1]))
                            merge(m1, m2)
                        }
                        ']' -> {
                            val m1 = move(row + dRow, col - 1, command, boxes + Box(row, col - 1, map[row][col - 1]))
                            val m2 = move(row + dRow, col, command, boxes + Box(row, col, map[row][col]))
                            merge(m1, m2)
                        }
                        '.' -> {
                            if (boxes.isEmpty()) {
                                Movement2.Robot(dRow = dRow, dCol = 0)
                            } else {
                                Movement2.RobotAndBoxes(boxes, dRow = dRow, dCol = 0)
                            }
                        }
                        '#' -> Movement2.NoMovement
                        else -> error("Oops")
                    }
                }
            }
            else -> error(command)
        }
    }

    private fun merge(m1: Movement2, m2: Movement2): Movement2 {
        return when (m1) {
            Movement2.NoMovement -> Movement2.NoMovement
            is Movement2.Robot -> m2
            is Movement2.RobotAndBoxes -> when (m2) {
                Movement2.NoMovement -> Movement2.NoMovement
                is Movement2.Robot -> m1
                is Movement2.RobotAndBoxes -> Movement2.RobotAndBoxes(m1.boxes + m2.boxes, m1.dRow, m1.dCol)
            }
        }
    }

    private fun getBoxes2(row: Int, col: Int, dRow: Int = 0, dCol: Int = 0): Movement2 {
        var r = row + dRow
        var c = col + dCol
        if (map[r][c] == '.') {
            return Movement2.Robot(dRow, dCol)
        }
        if (map[r][c] == '#') {
            return Movement2.NoMovement
        }

        val boxes = mutableSetOf<Box>()
        while (map[r][c] == '[' || map[r][c] == ']') {
            boxes += Box(r, c, map[r][c])
            r += dRow
            c += dCol
        }
        return if (map[r][c] == '.') {
            Movement2.RobotAndBoxes(boxes, dRow, dCol)
        } else {
            Movement2.NoMovement
        }
    }

    private sealed interface Movement {
        data class RobotAndBoxes(val boxes: List<Position>, val dRow: Int, val dCol: Int) : Movement
        data class Robot(val dRow: Int, val dCol: Int) : Movement
        object NoMovement : Movement
    }

    private sealed interface Movement2 {
        data class RobotAndBoxes(val boxes: Set<Box>, val dRow: Int, val dCol: Int) : Movement2
        data class Robot(val dRow: Int, val dCol: Int) : Movement2
        object NoMovement : Movement2
    }

    private data class Box(
        val row: Int,
        val col: Int,
        val value: Char
    )
}