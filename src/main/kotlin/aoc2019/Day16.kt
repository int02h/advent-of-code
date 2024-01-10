package aoc2019

import AocDay
import Input
import kotlin.concurrent.fixedRateTimer
import kotlin.math.abs
import kotlin.math.sign

object Day16 : AocDay {

    override fun part1(input: Input) {
        var signal = input.asText().trim().map { it.digitToInt() }
        val patterns = buildPatters(signal.size)
        repeat(100) {
            signal = runPhase(signal, patterns)
        }
        println(signal.take(8).joinToString(separator = ""))
    }

    override fun part2(input: Input) {
        val originalSignal = input.asText().trim().map { it.digitToInt() }
        val offset = originalSignal.take(7).joinToString(separator = "").toInt()
        val signal = RepeatedList(originalSignal, 10_000)

        val signalPart = mutableListOf<Int>()
        for (i in offset until signal.size) {
            signalPart += signal[i]
        }

        repeat(100) {
            for (i in (signalPart.lastIndex - 1) downTo 0) {
                signalPart[i] = abs(signalPart[i + 1] + signalPart[i]) % 10
            }
        }
        println(signalPart.take(8).joinToString(separator = ""))
    }

    private fun runPhase(signal: List<Int>, patters: List<Pattern>): List<Int> {
        val result = ArrayList<Int>(signal.size)
        repeat(signal.size) { outputIndex ->
            val pattern = patters[outputIndex]
            var sum = 0
            signal.forEachIndexed { inputIndex, value ->
                sum += value * pattern[inputIndex + 1]
            }
            result += (abs(sum) % 10)
        }
        return result
    }

    private fun hasRepeats(values: List<Int>, count: Int): Boolean {
        if (values.size % count != 0) {
            return false
        }
        val length = values.size / count
        val pattern = values.take(length)
        for (i in 1 until count) {
            val sublist = values.subList(i * length, (i + 1) * length)
            if (sublist != pattern) {
                return false
            }
        }
        return true
    }

    private fun buildPatters(size: Int): List<Pattern> {
        return (1..size).map { repeatCount -> Pattern(repeatCount) }
    }

    private fun <E> List<E>.repeat(count: Int): List<E> {
        val result = ArrayList<E>(size * count)
        repeat(count) {
            result.addAll(this)
        }
        return result
    }

    private class Pattern(val repeatCount: Int) {
        private val base = listOf(0, 1, 0, -1)

        val size = base.size * repeatCount

        operator fun get(index: Int): Int {
            return base[(index % size) / repeatCount]
        }
    }

    private class RepeatedList<E>(val original: List<E>, val repeatCount: Int) {
        val size = original.size * repeatCount

        operator fun get(index: Int): E {
            if (index >= size) {
                throw IndexOutOfBoundsException()
            }
            return original[index % original.size]
        }
    }
}
