package aoc2015

import AocDay
import Input
import kotlin.math.min

object Day17 : AocDay {

    override fun part1(input: Input) {
        val containers = input.asLines().map { it.toInt() }
        val maxValue = (1 shl containers.size) - 1
        val target = 150
        var count = 0

        for (value in 0..maxValue) {
            val sum = containers.foldIndexed(0) { index, acc, container -> acc + value.bit(index) * container }
            if (sum == target) {
                count++
            }
        }
        println(count)
    }

    override fun part2(input: Input) {
        val containers = input.asLines().map { it.toInt() }
        val maxValue = (1 shl containers.size) - 1
        val target = 150
        var minContainersCount = Int.MAX_VALUE

        for (value in 0..maxValue) {
            val sum = containers.foldIndexed(0) { index, acc, container -> acc + value.bit(index) * container }
            if (sum == target) {
                minContainersCount = min(minContainersCount, value.countOneBits())
            }
        }

        var count = 0
        for (value in 0..maxValue) {
            val sum = containers.foldIndexed(0) { index, acc, container -> acc + value.bit(index) * container }
            if (sum == target && value.countOneBits() == minContainersCount) {
                count++
            }
        }

        println(count)
    }

    private fun Int.bit(index: Int): Int {
        val mask = 1 shl index
        return if (this and mask == mask) 1 else 0
    }
}
