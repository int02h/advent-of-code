package aoc2022

import Input
import kotlin.math.ceil
import kotlin.math.max

object Day17 {

    private val PATTERNS = arrayOf(
        setOf(
            Position(0, 0),
            Position(1, 0),
            Position(2, 0),
            Position(3, 0),
        ),
        setOf(
            Position(1, 0),
            Position(0, 1),
            Position(1, 1),
            Position(2, 1),
            Position(1, 2),
        ),
        setOf(
            Position(0, 0),
            Position(1, 0),
            Position(2, 0),
            Position(2, 1),
            Position(2, 2),

            ),
        setOf(
            Position(0, 0),
            Position(0, 1),
            Position(0, 2),
            Position(0, 3),
        ),
        setOf(
            Position(0, 0),
            Position(1, 0),
            Position(0, 1),
            Position(1, 1),
        )
    )

    fun part1(input: Input) {
        var top = 0
        var gasIndex = 0
        val gas = input.asText().trim()
        val field = mutableMapOf<Position, Char>()

        repeat(2022) {
            val shape = Shape(field, PATTERNS[it % PATTERNS.size], 2, top + 4)
            do {
                shape.move(if (gas[(gasIndex++) % gas.length] == '>') 1 else -1, 0)
                val vMoved = shape.move(0, -1)
            } while (vMoved)
            shape.stop()
            top = max(top, shape.top)
        }
        println(top)
    }

    fun part2(input: Input) {
        var top = 0
        var gasIndex = 0
        val gas = input.asText().trim()
        val field = mutableMapOf<Position, Char>()
        val topDiffs = mutableListOf<Int>()
        var oldTop = 0
        var repeatingPatternLength: Int
        val nonPatternTop: Int
        val patternTop: Int

        println(gas.length)
        var shapeIndex = 0
        while (true) {
            repeat(gas.length) {
                val shape = Shape(field, PATTERNS[(shapeIndex++) % PATTERNS.size], 2, top + 4)
                do {
                    shape.move(if (gas[(gasIndex++) % gas.length] == '>') 1 else -1, 0)
                    val vMoved = shape.move(0, -1)
                } while (vMoved)
                shape.stop()
                top = max(top, shape.top)
            }
            topDiffs += (top - oldTop)
            oldTop = top

            repeatingPatternLength = findRepeatingPattern(topDiffs)
            if (repeatingPatternLength > 0) {
                val nonPatternLength = topDiffs.size % repeatingPatternLength
                nonPatternTop = topDiffs.take(nonPatternLength).sum()
                patternTop = topDiffs.takeLast(repeatingPatternLength).sum()
                break
            }
        }
//        val repeatCount = 2022L
        val repeatCount = 1000000000000L
        val patternCount = repeatCount / (repeatingPatternLength * gas.length)
        val rocksLeft = repeatCount - patternCount * (repeatingPatternLength * gas.length) - gas.length
        val nonFullPattern = rocksLeft.toDouble() / (repeatingPatternLength * gas.length)
        val totalTop = nonPatternTop + patternCount * patternTop + ceil(nonFullPattern * patternTop).toLong()
        println(totalTop)
    }

    private fun findRepeatingPattern(items: List<Int>): Int {
        var patternLength = 5
        while (items.size > 2 * patternLength) {
            val patternCandidate = items.takeLast(patternLength)
            for (i in (items.size - 2 * patternLength) downTo 0) {
                val sublist = items.subList(i, i + patternLength)
                if (sublist != patternCandidate) {
                    patternLength++
                    break
                } else {
                    return patternCandidate.size
                }
            }
        }
        return 0
    }

    private class Shape(
        val field: MutableMap<Position, Char>,
        val pattern: Set<Position>,
        var x: Int,
        var y: Int
    ) {

        val top: Int get() = y + pattern.maxOf { it.y }

        fun move(dx: Int, dy: Int): Boolean {
            val canMove = pattern.all { p ->
                val pos = Position(x + p.x + dx, y + p.y + dy)
                (pos.x in 0..6) && (pos.y > 0) && (field[pos] == null)
            }
            if (canMove) {
                x += dx
                y += dy
            }
            return canMove
        }

        fun stop() {
            pattern.forEach { p ->
                field[Position(x + p.x, y + p.y)] = '#'
            }
        }
    }

    private data class Position(val x: Int, val y: Int)
}