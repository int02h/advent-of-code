package aoc2024

import AocDay
import Input
import kotlin.math.max

object Day9 : AocDay {

    override fun part1(input: Input) {
        val blocks = buildBlock(input)

        var indexFrom = blocks.indexOfLast { it != -1 }
        var indexTo = blocks.indexOfFirst { it == -1 }
        while (indexFrom > indexTo) {
            blocks[indexTo] = blocks[indexFrom]
            blocks[indexFrom] = -1
            indexFrom = blocks.indexOfLast { it != -1 }
            indexTo = blocks.indexOfFirst { it == -1 }
        }

        println(calculateCheckSum(blocks))
    }

    override fun part2(input: Input) {
        val blocks = buildBlock(input)

        fun findEmptySpace(size: Int): Int {
            var count = 0
            var startIndex = -1
            for ((index, id) in blocks.withIndex()) {
                if (id == -1) {
                    count++
                    if (startIndex == -1) {
                        startIndex = index
                    }
                    if (count == size) {
                        return startIndex
                    }
                } else {
                    count = 0
                    startIndex = -1
                }
            }
            return -1
        }

        fun getFile(id: Int): Pair<Int, Int> {
            val start = blocks.indexOf(id)
            val end = blocks.lastIndexOf(id)
            return start to end
        }

        fun moveFile(file: Pair<Int, Int>, index: Int) {
            val id = blocks[file.first]
            val size = file.second - file.first + 1
            for (i in index until (index + size)) {
                blocks[i] = id
            }
            for (i in file.first..file.second) {
                blocks[i] = -1
            }
        }

        var fileId = blocks.last()
        while (fileId >= 0) {
            val file = getFile(fileId)
            val indexTo = findEmptySpace(file.second - file.first + 1)
            if (indexTo != -1 && indexTo < file.first) {
                moveFile(file, indexTo)
            }
            fileId--
        }

        println(calculateCheckSum(blocks))
    }

    private fun calculateCheckSum(blocks: MutableList<Int>): Long {
        return blocks.mapIndexed { index, id -> index * max(id.toLong(), 0L) }.sum()
    }

    private fun buildBlock(input: Input): MutableList<Int> {
        val diskMap = input.asText().trim().map { ch -> ch.digitToInt() }
        val blocks = mutableListOf<Int>()
        var fileId = 0
        diskMap.forEachIndexed { index, count ->
            if (index % 2 == 0) {
                repeat(count) {
                    blocks += fileId
                }
                fileId++
            } else {
                repeat(count) {
                    blocks += -1
                }
            }
        }
        return blocks
    }

    private fun debug(block: List<Int>) {
        block.forEach { id ->
            if (id == -1) {
                print('.')
            } else {
                print(id)
            }
        }
        println()
    }

}