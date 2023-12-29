package aoc2019

import AocDay
import Input
import kotlin.math.max

object Day7 : AocDay {
    override fun part1(input: Input) {
        val program = readProgram(input)
        val amps = (1..5).map { Amp(program) }.toTypedArray()
        val initialPhases = intArrayOf(0, 1, 2, 3, 4)
        val phases = initialPhases.copyOf()
        var max = 0
        do {
            max = max(getOutput(amps, phases), max)
            nextPhases(phases, 0..4)
        } while (!initialPhases.contentEquals(phases))
        println(max)
    }

    override fun part2(input: Input) {
        val program = readProgram(input)
        val amps = (1..5).map { Amp(program) }.toTypedArray()
        val initialPhases = intArrayOf(5, 6, 7, 8, 9)
        val phases = initialPhases.copyOf()
        var max = 0
        do {
            max = max(getOutput2(amps, phases), max)
            nextPhases(phases, 5..9)
        } while (!initialPhases.contentEquals(phases))
        println(max)
    }

    private fun getOutput(amps: Array<Amp>, phases: IntArray): Int {
        var input = 0
        amps.forEachIndexed { index, amp ->
            input = amp.handle(input = input, phase = phases[index])
        }
        return input
    }

    private fun getOutput2(amps: Array<Amp>, phases: IntArray): Int {
        amps.forEachIndexed { index, amp -> amp.prepare(phases[index]) }

        var input = 0
        var index = 0
        while (true) {
            amps[index].runUntilOutput(input)
            input = amps[index].output.last()
            index++
            if (index == amps.size) {
                if (amps.last().isFinished) {
                    break
                }
                index = 0
            }
        }
        return input
    }

    private fun nextPhases(phases: IntArray, range: IntRange) {
        var i: Int
        do {
            i = phases.lastIndex
            while (i >= 0) {
                phases[i]++
                if (phases[i] == range.last + 1) {
                    phases[i] = range.first
                } else {
                    break
                }
                i--
            }
        } while (phases.toSet().size != phases.size)
    }

    private class Amp(val program: IntArray) {

        private lateinit var computer: IntCodeComputer
        val output = mutableListOf<Int>()
        val input = mutableListOf<Int>()

        val isFinished: Boolean get() = computer.position == -1

        fun handle(input: Int, phase: Int): Int {
            val output = mutableListOf<Int>()
            IntCodeComputer(
                program = program.copyOf(),
                input = mutableListOf(phase, input),
                output = output
            ).runAll()
            assert(output.size == 1)
            return output.first()
        }

        fun prepare(phase: Int) {
            output.clear()
            input.clear()
            input += phase
            computer = IntCodeComputer(program = program.copyOf(), input = input, output = output)
        }

        fun runUntilOutput(input: Int) {
            this.input += input
            computer.runUntilOutput()
        }
    }

    private fun readProgram(input: Input): IntArray =
        input.asText().trim().split(",").map { it.trim().toInt() }.toIntArray()
}