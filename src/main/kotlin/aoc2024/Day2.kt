package aoc2024

import AocDay
import Input
import kotlin.math.abs

object Day2 : AocDay {
    override fun part1(input: Input) {
        val reports = readReports(input)
        println(reports.count { it.isSafe() })
    }

    override fun part2(input: Input) {
        val reports = readReports(input)
        println(reports.count { it.isSafe2() })
    }

    private fun readReports(input: Input): List<Report> =
        input.asLines().map { Report(it.split(" ").map { it.trim().toInt() }) }

    class Report(private val levels: List<Int>) {

        fun isSafe(): Boolean {
            return checkIfSafe(levels)
        }

        fun isSafe2(): Boolean {
            if (checkIfSafe(levels)) {
                return true
            }

            for (i in levels.indices) {
                val copy = levels.toMutableList()
                copy.removeAt(i)
                if (checkIfSafe(copy)) {
                    return true
                }
            }

            return false
        }

        companion object {
            private fun checkIfSafe(levels: List<Int>): Boolean {
                val diffs = buildDiffs(levels)
                val isIncreasing = diffs.all { it > 0 }
                val isDecreasing = diffs.all { it < 0 }
                if (!isIncreasing && !isDecreasing) {
                    return false
                }
                return diffs.maxOf { abs(it) } <= 3
            }

            private fun buildDiffs(levels: List<Int>): List<Int> {
                val diffs = mutableListOf<Int>()
                for (i in 1 until levels.size) {
                    diffs += levels[i] - levels[i - 1]
                }
                return diffs
            }
        }

    }
}