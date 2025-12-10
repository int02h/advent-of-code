package aoc2025

import AocDay2
import Input
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

import util.RowColPosition as Pos

class Day9 : AocDay2 {

    private lateinit var tiles: List<Pos>

    override fun readInput(input: Input) {
        tiles = input.asLines()
            .map { line -> line.split(",") }
            .map { (col, row) -> Pos(row.toInt(), col.toInt()) }
    }

    override fun part1() {
        var largest = 0L
        for (i in tiles.indices) {
            for (j in (i + 1) until tiles.size) {
                val width = (abs(tiles[i].col - tiles[j].col) + 1).toLong()
                val height = (abs(tiles[i].row - tiles[j].row) + 1).toLong()
                val area = width * height
                largest = max(largest, area)
            }
        }
        println(largest)
    }

    override fun part2() {
        val borderLeft = mutableMapOf<Int, Int>()
        val borderRight = mutableMapOf<Int, Int>()
        val lines = mutableListOf<Pair<Pos, Pos>>()
        for (i in tiles.indices) {
            val prev = tiles[(i - 1 + tiles.size) % tiles.size]
            val cur = tiles[i]
            if (prev.row == cur.row) {
                lines += prev to cur
            } else {
                for (row in min(prev.row, cur.row)..max(prev.row, cur.row)) {
                    borderLeft[row] = min(cur.col, borderLeft.getOrDefault(row, Int.MAX_VALUE))
                    borderRight[row] = max(cur.col, borderRight.getOrDefault(row, 0))
                }
            }
        }

        lines.sortByDescending { abs(it.first.col - it.second.col) }
        val startPoints = listOf(
            lines[0].toList().maxBy { it.col },
            lines[1].toList().maxBy { it.col }
        )

        val isInside: (Int, Int) -> Boolean = { row, col ->
            col >= borderLeft.getValue(row) && col <= borderRight.getValue(row)
        }

        val lowRow = startPoints.minOf { it.row }
        val (lowRect, lowLargest) = findLargest(tiles.filter { it.row <= lowRow }, isInside)

        val highRow = startPoints.maxOf { it.row }
        val (highRect, highLargest) = findLargest(tiles.filter { it.row >= highRow }, isInside)

//        if (lowLargest > highLargest) {
//            drawField(startPoints, lowRect.first, lowRect.second)
//        } else {
//            drawField(startPoints, highRect.first, highRect.second)
//        }

        println(max(lowLargest, highLargest))
    }

    private fun findLargest(tiles: List<Pos>, isInside: (Int, Int) -> Boolean): Pair<Pair<Pos, Pos>, Long> {
        var largest = 0L
        lateinit var pair: Pair<Pos, Pos>
        for (i in tiles.indices) {
            val ti = tiles[i]
            for (j in (i + 1) until tiles.size) {
                val tj = tiles[j]
                val bottom = min(ti.row, tj.row)
                val left = min(ti.col, tj.col)
                val top = max(ti.row, tj.row)
                val right = max(ti.col, tj.col)
                if (
                    isInside(bottom, left) &&
                    isInside(bottom, right) &&
                    isInside(top, left) &&
                    isInside(top, right) &&
                    isNoTilesInside(tiles, top, right, bottom, left)
                ) {
                    val width = (abs(ti.col - tj.col) + 1).toLong()
                    val height = (abs(ti.row - tj.row) + 1).toLong()
                    val area = width * height
                    if (area > largest) {
                        largest = area
                        pair = ti to tj
                    }
                }
            }
        }
        return pair to largest
    }

    private fun isNoTilesInside(tiles: List<Pos>, top: Int, right: Int, bottom: Int, left: Int): Boolean {
        for (t in tiles) {
            if (t.row < top && t.row > bottom && t.col > left && t.col < right) return false
        }
        return true
    }

    private fun drawField(startPoints: List<Pos>, rectStart: Pos, rectEnd: Pos) {
        val field = mutableMapOf<Pos, Int>()
        for (i in 0 until tiles.size) {
            val prev = tiles[(i - 1 + tiles.size) % tiles.size]
            val cur = tiles[i]
            if (prev.col == cur.col) {
                for (row in min(prev.row, cur.row)..max(prev.row, cur.row)) {
                    field[Pos(row / 10, cur.col / 10)] = 0x00FF00
                }
            } else {
                for (col in min(prev.col, cur.col)..max(prev.col, cur.col)) {
                    field[Pos(cur.row / 10, col / 10)] = 0x00FF00
                }
            }
        }

        tiles.forEach { t ->
            field[Pos(t.row / 10, t.col / 10)] = 0xFF0000
        }
        startPoints.forEach { p ->
            field[Pos(p.row / 10, p.col / 10)] = 0x0000FF
        }

        val img = BufferedImage(
            field.keys.maxOf { it.col } - field.keys.minOf { it.col } + 1,
            field.keys.maxOf { it.row } - field.keys.minOf { it.row } + 1,
            BufferedImage.TYPE_INT_RGB
        )

        var x = 0
        var y = 0
        for (row in field.keys.minOf { it.row }..field.keys.maxOf { it.row }) {
            for (col in field.keys.minOf { it.col }..field.keys.maxOf { it.col }) {
                val color = field[Pos(row, col)] ?: 0
                img.setRGB(x, y, color)
                x++
            }
            y++
            x = 0
        }

        img.createGraphics().let { g ->
            val sCol = field.keys.minOf { it.col }
            val sRow = field.keys.minOf { it.row }
            g.color = Color.WHITE
            val bottom = min(rectStart.row, rectEnd.row)
            val left = min(rectStart.col, rectEnd.col)
            val width = abs(rectStart.col - rectEnd.col) + 1
            val height = abs(rectStart.row - rectEnd.row) + 1
            g.drawRect(
                left / 10 - sCol,
                bottom / 10 - sRow,
                width / 10,
                height / 10
            )
            g.dispose()
        }

        ImageIO.write(img, "png", File("./field.png"))
    }

}