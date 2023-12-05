package aoc2023

import AocDay
import Input

object Day5 : AocDay {
    override fun part1(input: Input) {
        val data = parse(input)
        println(
            data.seeds.minOf { s ->
                data.mappers.fold(s) { acc, mapper -> mapper.map(acc) }
            }
        )
    }

    override fun part2(input: Input) {
        val data = parse(input)
        val seedRanges = mutableListOf<LongRange>()
        repeat(data.seeds.size / 2) {
            val start = data.seeds[2 * it]
            val length = data.seeds[2 * it + 1]
            seedRanges += (start until start + length)
        }

        val locations = mutableListOf<Long>()
        val threads = seedRanges.map { sr ->
            Thread {
                val min = sr.minOf { s ->
                    data.mappers.fold(s) { acc, mapper -> mapper.map(acc) }
                }
                synchronized(locations) {
                    locations += min
                }
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        println(locations.min())
    }

    private fun parse(input: Input): InputData {
        val lines = input.asLines()
        val seeds = lines[0].split(": ")[1].split(" ").map { it.toLong() }

        var currentMapper = Mapper()
        val mappers = mutableListOf(currentMapper)

        val mapperLines = lines.drop(3).filter { it.trim().isNotEmpty() }
        mapperLines.forEach { ml ->
            if (ml.endsWith("map:")) {
                currentMapper = Mapper()
                mappers += currentMapper
            } else {
                val (dstStart, srcStart, length) = ml.split(" ").map { it.toLong() }
                currentMapper.addDestinationRange(dstStart until dstStart + length)
                currentMapper.addSourceRange(srcStart until srcStart + length)
            }
        }

        return InputData(seeds, mappers)
    }

    private data class InputData(val seeds: List<Long>, val mappers: List<Mapper>)

    private class Mapper {
        private val dstRanges = mutableListOf<LongRange>()
        private val srcRanges = mutableListOf<LongRange>()

        fun addDestinationRange(range: LongRange) {
            dstRanges += range
//            addRange(dstRanges, range)
        }

        fun addSourceRange(range: LongRange) {
            srcRanges += range
//            addRange(srcRanges, range)
        }

        fun map(seed: Long): Long {
            val srcIndex = srcRanges.indexOfFirst { it.contains(seed) }
            if (srcIndex < 0) {
                return seed
            }
            val srcRange = srcRanges[srcIndex]
            val dstRange = dstRanges[srcIndex]
            val diff = seed - srcRange.first
            return dstRange.first + diff
        }

        companion object {
            private fun addRange(list: MutableList<LongRange>, range: LongRange) {
                var r = list.find { it.last + 1 == range.first }
                if (r != null) {
                    list.remove(r)
                    list += r.first..range.last
                } else {
                    r = list.find { it.first == range.last + 1 }
                    if (r != null) {
                        list.remove(r)
                        list += range.first..r.last
                    } else {
                        list += range
                    }
                }
            }
        }
    }
}