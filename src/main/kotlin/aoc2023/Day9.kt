package aoc2023

import AocDay
import Input

object Day9 : AocDay {

    override fun part1(input: Input) {
        val dataset = parse(input)
        println(
            dataset.map { HistoryLine(it) }.sumOf { it.extrapolateLast() }
        )
    }

    override fun part2(input: Input) {
        val dataset = parse(input)
        println(
            dataset.map { HistoryLine(it) }.sumOf { it.extrapolateFirst() }
        )
    }

    private fun parse(input: Input): List<List<Int>> =
        input.asLines().map { line ->
            line.split(" ").map { it.toInt() }
        }

    class HistoryLine(val value: List<Int>) {

        val diffLines = mutableListOf<MutableList<Int>>()

        init {
            diffLines.add(value.toMutableList())
            var line = value
            do {
                val diffLine = mutableListOf<Int>()
                for (i in 1..line.lastIndex) {
                    diffLine += line[i] - line[i - 1]
                }
                line = diffLine
                diffLines += diffLine
            } while (!diffLine.all { it == 0 })
        }

        fun extrapolateLast(): Int {
            var diff = 0
            for (i in (diffLines.lastIndex - 1) downTo 0) {
                diffLines[i] += diffLines[i].last() + diff
                diff = diffLines[i].last()
            }

            return diffLines.first().last()
        }

        fun extrapolateFirst(): Int {
            var diff = 0
            for (i in (diffLines.lastIndex - 1) downTo 0) {
                diffLines[i].add(0, diffLines[i].first() - diff)
                diff = diffLines[i].first()
            }

            return diffLines.first().first()
        }
    }
}