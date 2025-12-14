package aoc2017

import AocDay2
import Input

class Day5 : AocDay2 {

    private lateinit var offsets: IntArray

    override fun readInput(input: Input) {
        offsets = input.asLines().map { it.toInt() }.toIntArray()
    }

    override fun part1() {
        var pos = 0
        var count = 0
        while (true) {
            val offset = offsets[pos]
            offsets[pos]++
            pos += offset
            count++
            if (pos < 0 || pos > offsets.lastIndex) {
                break
            }
        }
        println(count)
    }

    override fun part2() {
        var pos = 0
        var count = 0
        while (true) {
            val offset = offsets[pos]
            if (offset >= 3) {
                offsets[pos]--
            } else {
                offsets[pos]++
            }
            pos += offset
            count++
            if (pos < 0 || pos > offsets.lastIndex) {
                break
            }
        }
        println(count)
    }

}