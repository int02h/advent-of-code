package aoc2022

import Input
import kotlin.math.max

object Day8 {

    fun part1(input: Input) {
        val field = input.asLines().map { line -> line.map { Tree(it.digitToInt()) } }
        val columnCount = field.first().size
        field.forEach { row ->
            var maxHeightToRight = -1
            var maxHeightToLeft = -1
            for (col in 0 until columnCount) {
                if (row[col].height > maxHeightToRight) {
                    row[col].isVisible = true
                    maxHeightToRight = row[col].height
                }
                if (row[columnCount - col - 1].height > maxHeightToLeft) {
                    row[columnCount - col - 1].isVisible = true
                    maxHeightToLeft = row[columnCount - col - 1].height
                }
            }
        }

        for (col in 0 until columnCount) {
            var maxHeightToBottom = -1
            var maxHeightToTop = -1
            for (row in 0..field.lastIndex) {
                if (field[row][col].height > maxHeightToBottom) {
                    field[row][col].isVisible = true
                    maxHeightToBottom = field[row][col].height
                }
                if (field[field.lastIndex - row][col].height > maxHeightToTop) {
                    field[field.lastIndex - row][col].isVisible = true
                    maxHeightToTop = field[field.lastIndex - row][col].height
                }
            }
        }

        println(
            field.sumOf { row -> row.count { tree -> tree.isVisible } }
        )
    }

    fun part2(input: Input) {
        val field = input.asLines().map { line -> line.map { Tree(it.digitToInt()) } }
        var maxScore = 0
        for (row in 0..field.lastIndex) {
            for (col in 0..field.first().lastIndex) {
                maxScore = max(getScenicScore(field, row, col), maxScore)
            }
        }
        println(maxScore)
    }

    private fun getScenicScore(field: List<List<Tree>>, row: Int, col: Int): Int {
        val currentHeight = field[row][col].height

        var upScore = 0
        for (y in row - 1 downTo 0) {
            if (field[y][col].height >= currentHeight) {
                upScore++
                break
            }
            upScore++
        }

        var downScore = 0
        for (y in row + 1..field.lastIndex) {
            if (field[y][col].height >= currentHeight) {
                downScore++
                break
            }
            downScore++
        }

        var leftScore = 0
        for (x in col - 1 downTo 0) {
            if (field[row][x].height >= currentHeight) {
                leftScore++
                break
            }
            leftScore++
        }

        var rightScore = 0
        for (x in col + 1..field.first().lastIndex) {
            if (field[row][x].height >= currentHeight) {
                rightScore++
                break
            }
            rightScore++
        }

        return upScore * downScore * leftScore * rightScore
    }

    class Tree(val height: Int, var isVisible: Boolean = false)

}