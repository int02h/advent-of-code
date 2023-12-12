package aoc2023

import AocDay
import Input

object Day12 : AocDay {

    override fun part1(input: Input) {
        val records = parse(input)
        println(records.sumOf { it.countSolutions() })
    }

    override fun part2(input: Input) {
        val records = parse(input).map { it.unfold() }
        println(records.sumOf { it.countSolutions() })
    }

    private fun parse(input: Input): List<Record> =
        input.asLines().map { line ->
            val (springs, damaged) = line.split(" ")
            Record(springs, damaged.split(",").map { it.toInt() })
        }

    private class Record(val springs: String, val damaged: List<Int>) {

        private val cache = mutableMapOf<CacheKey, Long>()

        fun unfold(): Record {
            val newDamaged = mutableListOf<Int>()
            var newSprings = ""
            var separator = ""
            repeat(5) {
                newDamaged.addAll(damaged)
                newSprings += separator + springs
                separator = "?"
            }
            return Record(newSprings, newDamaged)
        }

        fun countSolutions(): Long {
            return countSolutions("$springs.", damaged, 0)
        }

        private fun countSolutions(springs: String, damaged: List<Int>, currentGroupSize: Int): Long {
            val key = CacheKey(springs, damaged, currentGroupSize)
            val cached = cache[key]
            if (cached != null) {
                return cached
            }

            if (springs.isEmpty()) {
                if (damaged.isEmpty() && currentGroupSize == 0) {
                    return 1
                }
                return 0
            }
            var result = 0L
            val options = if (springs[0] == '?') listOf('.', '#') else listOf(springs[0])
            for (opt in options) {
                if (opt == '#') {
                    result += countSolutions(springs.drop(1), damaged, currentGroupSize + 1)
                } else {
                    if (currentGroupSize != 0) {
                        if (damaged.firstOrNull() == currentGroupSize) {
                            result += countSolutions(springs.drop(1), damaged.drop(1), 0)
                        }
                    } else {
                        result += countSolutions(springs.drop(1), damaged, 0)
                    }
                }
            }

            cache[key] = result
            return result
        }

        private data class CacheKey(val springs: String, val damaged: List<Int>, val currentGroupSize: Int)

    }
}