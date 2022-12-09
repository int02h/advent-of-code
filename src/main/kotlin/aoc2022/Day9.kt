package aoc2022

import Input
import kotlin.math.abs
import kotlin.math.sign

object Day9 {

    fun part1(input: Input) {
        simulate(2, input)
    }

    fun part2(input: Input) {
        simulate(10, input)
    }

    private fun simulate(ropeSize: Int, input: Input) {
        val rope = Array(ropeSize) { Knot(Position(0, 0)) }
        val visitedPosition = mutableSetOf<Position>()
        visitedPosition += rope.last().pos

        input.asLines()
            .map { it.split(' ') }
            .forEach { (direction, stepCount) ->
                repeat(stepCount.toInt()) {
                    rope.first().move(direction)
                    for (i in 1..rope.lastIndex) {
                        rope[i].moveTowards(rope[i - 1])
                    }
                    visitedPosition += rope.last().pos
                }
            }

        println(visitedPosition.size)
    }

    data class Position(val x: Int, val y: Int)

    class Knot(var pos: Position) {

        fun move(direction: String) {
            when (direction) {
                "R" -> pos = pos.copy(x = pos.x + 1)
                "L" -> pos = pos.copy(x = pos.x - 1)
                "U" -> pos = pos.copy(y = pos.y + 1)
                "D" -> pos = pos.copy(y = pos.y - 1)
            }
        }

        fun moveTowards(knot: Knot) {
            val dx = knot.pos.x - pos.x
            val dy = knot.pos.y - pos.y
            if (abs(dx) > 1 || abs(dy) > 1) {
                pos = Position(pos.x + dx.sign, pos.y + dy.sign)
            }
        }

        override fun toString(): String = "${pos.x},${pos.y}"
    }

}