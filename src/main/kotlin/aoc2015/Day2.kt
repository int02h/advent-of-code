package aoc2015

import Input
import kotlin.math.min

object Day2 {

    fun part1(input: Input) {
        var result = 0
        input.asLines()
            .map { line -> line.split('x') }
            .map { values -> values.map { it.toInt() } }
            .forEach { (l, w, h) ->
                result += 2 * l * w + 2 * w * h + 2 * h * l + min(l * w, min(w * h, h * l))
            }
        println(result)
    }

    fun part2(input: Input) {
        var result = 0
        input.asLines()
            .map { line -> line.split('x') }
            .map { values -> values.map { it.toInt() } }
            .forEach { (l, w, h) ->
                result += l * w * h + min(2 * l + 2 * w, min(2 * w + 2 * h, 2 * h + 2 * l))
            }
        println(result)
    }

}