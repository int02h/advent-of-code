package aoc2016

import AocDay2
import Input
import util.findAllNumbers

class Day15 : AocDay2 {

    private val discs = mutableListOf<Disc>()

    override fun readInput(input: Input) {
        discs += input.asLines().map { line ->
            val (_, positionCount, _, startPosition) = line.findAllNumbers()
            Disc(positionCount, startPosition)
        }
    }

    override fun part1() {
        println(getTime())
    }

    override fun part2() {
        discs += Disc(11, 0)
        println(getTime())
    }

    private fun getTime(): Int {
        var time = 0
        while (true) {
            var success = true
            for ((index, disc) in discs.withIndex()) {
                if ((disc.position + index + 1) % disc.positionCount != 0) {
                    success = false
                }
            }
            if (success) {
                return time
            }
            discs.forEach { it.rotate() }
            time++
        }
    }

    private class Disc(val positionCount: Int, startPosition: Int) {
        var position = startPosition

        fun rotate() {
            position = (position + 1) % positionCount
        }
    }
}
