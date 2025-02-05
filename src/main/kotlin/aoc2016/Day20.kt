package aoc2016

import AocDay2
import Input
import kotlin.math.min

class Day20 : AocDay2 {

    private lateinit var blacklist: List<LongRange>

    override fun readInput(input: Input) {
        blacklist = input.asLines()
            .map { line ->
                val (start, end) = line.split("-")
                start.toLong()..end.toLong()
            }
    }

    override fun part1() {
        var lowest = 0L
        do {
            var isChanged = false
            blacklist.forEach { rule ->
                if (rule.first <= lowest && rule.last >= lowest) {
                    lowest = rule.last + 1
                    isChanged = true
                }
            }
        } while (isChanged)
        println(lowest)
    }

    override fun part2() {
        val allowed = mutableListOf(0..4294967295)
        val toAdd = mutableListOf<LongRange>()

        blacklist.forEach { rule ->
            val it = allowed.iterator()
            toAdd.clear()
            while (it.hasNext()) {
                val allowedRange = it.next()
                if (rule.first in allowedRange && rule.last in allowedRange) {
                    it.remove()
                    toAdd += allowedRange.first..(rule.first - 1)
                    toAdd += (rule.last + 1)..allowedRange.last
                    continue
                }
                if (allowedRange.first in rule && allowedRange.last in rule) {
                    it.remove()
                    continue
                }
                if (rule.last < allowedRange.first || rule.first > allowedRange.last) {
                    continue
                }
                if (rule.first in allowedRange) {
                    it.remove()
                    toAdd += allowedRange.first..(rule.first - 1)
                    continue
                }
                if (rule.last in allowedRange) {
                    it.remove()
                    toAdd += (rule.last + 1)..allowedRange.last
                    continue
                }
            }
            allowed += toAdd.filter { !it.isEmpty() }
        }

        println(allowed.sumOf { it.first - it.last + 1 })
    }

}
