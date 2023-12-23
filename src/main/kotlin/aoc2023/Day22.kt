package aoc2023

import AocDay
import Input

object Day22 : AocDay {

    override fun part1(input: Input) {
        val bricks = parse(input)
        val cube = Cube()
        bricks.forEach { it.fallDown(cube) }

        val unsafe = mutableSetOf<Brick>()
        bricks.forEach { b ->
            if (b.supportedBy.size == 1) {
                unsafe.add(b.supportedBy.first())
            }
        }
        println(bricks.size - unsafe.size)
    }

    override fun part2(input: Input) {
        val bricks = parse(input)
        val cube = Cube()
        bricks.forEach { it.fallDown(cube) }

        val unsafe = mutableSetOf<Brick>()
        bricks.forEach { b ->
            if (b.supportedBy.size == 1) {
                unsafe.add(b.supportedBy.first())
            }
        }

        val wouldFail = mutableListOf<Brick>()
        unsafe.forEach { b ->
            wouldFail.addAll(b.getAllAbove())
        }
        println(wouldFail.size)
    }

    private fun parse(input: Input): List<Brick> {
        var letter = 'A'
        return input.asLines()
            .map { line ->
                val (start, end) = line.split("~")
                Brick(Position.parse(start), Position.parse(end), letter++)
            }
            .sortedBy { it.end.z }
    }

    private fun List<Position>.decZ(): List<Position> = map { it.decZ() }

    private data class Brick(val start: Position, val end: Position, val label: Char) {
        val supportedBy = mutableSetOf<Brick>()
        val support = mutableSetOf<Brick>()

        init {
            if (start.x > end.x) error("x")
            if (start.y > end.y) error("y")
            if (start.z > end.z) error("z")
        }

        fun fallDown(cube: Cube) {
            var cells = if (start.x == end.x && start.y == end.y) { // vertical
                (start.z..end.z).map { Position(start.x, start.y, it) }
            } else if (start.x == end.x && start.z == end.z) {
                (start.y..end.y).map { Position(start.x, it, start.z) }
            } else if (start.y == end.y && start.z == end.z) {
                (start.x..end.x).map { Position(it, start.y, start.z) }
            } else {
                error("Oops")
            }
            while (cube.isEmptyCells(cells.decZ())) {
                cells = cells.decZ()
            }
            cube.putBrick(cells, this)
        }

        fun getAllAbove(): Collection<Brick> {
            val result = mutableSetOf<Brick>()
            fun walk(brick: Brick) {
                result += brick
                brick.support.forEach {
                    if (it.supportedBy.all { sb -> result.contains(sb) }) {
                        walk(it)
                    }
                }
            }
            walk(this)
            result.remove(this)
            return result
        }

    }

    private class Cube {
        val cells = mutableMapOf<Position, Brick>()

        fun isEmptyCell(pos: Position): Boolean {
            if (pos.z <= 0) {
                return false
            }
            return !cells.contains(pos)
        }

        fun isEmptyCells(positions: List<Position>): Boolean = positions.all { isEmptyCell(it) }

        fun putBrick(brickCells: List<Position>, brick: Brick) {
            val supportBricks = brickCells.mapNotNull { bc -> cells[bc.decZ()] }
            brickCells.forEach {
                if (cells.put(it, brick) != null) error("Oops")
            }
            brick.supportedBy += supportBricks
            supportBricks.forEach { sp -> sp.support.add(brick) }
        }
    }

    private data class Position(val x: Int, val y: Int, val z: Int) {
        fun decZ() = copy(z = z - 1)

        companion object {
            fun parse(value: String): Position {
                val (x, y, z) = value.split(",").map { it.toInt() }
                return Position(x, y, z)
            }
        }
    }

}
