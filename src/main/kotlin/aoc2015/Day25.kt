package aoc2015

import AocDay2
import Input
import util.findAllNumbers

class Day25 : AocDay2 {

    private var targetRow = -1
    private var targetCol = -1

    override fun readInput(input: Input) {
        val (row, col) = input.asText().findAllNumbers()
        targetRow = row
        targetCol = col
    }

    override fun part1() {
        var row = 1
        var col = 1
        var code = 20151125L
        do {
            col += 1
            row -= 1
            if (row == 0) {
                row = col
                col = 1
            }
            code = (code * 252533) % 33554393
        } while (row != targetRow || col != targetCol)
        println(code)
    }

    override fun part2() {
    }
}