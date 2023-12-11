package aoc2023

import AocDay
import Input
import kotlin.math.abs

object Day11 : AocDay {

    override fun part1(input: Input) {
        calculate(input, 2)
    }

    override fun part2(input: Input) {
        calculate(input, 1_000_000)
    }

    private fun calculate(input: Input, ratio: Int) {
        val image = parse(input)
        expand(image, ratio)

        val pairs = mutableListOf<Pair<Point, Point>>()
        val galaxies = image.toList()
        for (i in galaxies.indices) {
            for (j in (i + 1)..galaxies.lastIndex) {
                pairs += galaxies[i] to galaxies[j]
            }
        }

        println(pairs.sumOf { it.first.distanceTo(it.second) })
    }

    private fun parse(input: Input): MutableSet<Point> {
        val lines = input.asLines()
        val image = mutableSetOf<Point>()

        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, ch ->
                if (ch == '#') {
                    image += Point(row.toLong(), col.toLong())
                }
            }
        }

        return image
    }

    private fun expand(image: MutableSet<Point>, ratio: Int) {
        var width = image.maxOf { it.col } + 1
        var height = image.maxOf { it.row } + 1
        val shift = ratio - 1

        var row = 0L
        while (row < height) {
            if ((0 until width).all { !image.contains(Point(row, it)) }) { // empty row
                val shifted = image.filter { it.row > row }
                image.removeAll(shifted.toSet())
                image.addAll(shifted.map { Point(it.row + shift, it.col) })
                height += shift
                row += shift
            }
            row++
        }

        var col = 0L
        while (col < width) {
            if ((0 until height).all { !image.contains(Point(it, col)) }) { // empty column
                val shifted = image.filter { it.col > col }
                image.removeAll(shifted.toSet())
                image.addAll(shifted.map { Point(it.row, it.col + shift) })
                width += shift
                col += shift
            }
            col++
        }
    }

    private data class Point(val row: Long, val col: Long) {
        fun distanceTo(other: Point): Long = abs(other.row - this.row) + abs(other.col - this.col)
    }
}