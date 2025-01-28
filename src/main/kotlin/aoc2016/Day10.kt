package aoc2016

import AocDay2
import Input
import util.findAllNumbers

class Day10 : AocDay2 {

    private lateinit var bots: List<Bot>
    private val outputs = mutableMapOf<Int, Int>()

    override fun readInput(input: Input) {
        val map = mutableMapOf<Int, Bot>()
        input.asLines().forEach { line ->
            val values = line.findAllNumbers()
            if (line.startsWith("value")) {
                map.getOrPut(values[1]) { Bot(values[1], map, outputs) }.addChip(values[0])
            } else {
                var lowTo: Output = Output.Bot(values[1])
                var highTo: Output = Output.Bot(values[2])
                if (line.contains("low to output")) {
                    lowTo = Output.Output(values[1])
                }
                if (line.contains("high to output")) {
                    highTo = Output.Output(values[2])
                }
                map.getOrPut(values[0]) { Bot(values[0], map, outputs) }.configure(lowTo, highTo)
            }
        }
        bots = map.values.toList()
    }

    override fun part1() {
        var canExecute = bots.filter { it.canExecute }
        while (canExecute.isNotEmpty()) {
            for (it in canExecute) {
                if (it.execute()) {
                    println(it.id)
                    return
                }
            }
            canExecute = bots.filter { it.canExecute }
        }
    }

    override fun part2() {
        var canExecute = bots.filter { it.canExecute }
        while (canExecute.isNotEmpty()) {
            canExecute.forEach { it.execute() }
            if (outputs.containsKey(0) && outputs.containsKey(1) && outputs.containsKey(2)) {
                break
            }
            canExecute = bots.filter { it.canExecute }
        }
        println(outputs.getValue(0) * outputs.getValue(1) * outputs.getValue(2))
    }

    private class Bot(
        val id: Int,
        private val bots: Map<Int, Bot>,
        private val outputs: MutableMap<Int, Int>
    ) {

        private val values = mutableListOf<Int>()
        private lateinit var lowTo: Output
        private lateinit var highTo: Output

        val canExecute: Boolean
            get() = values.size >= 2

        fun addChip(value: Int) {
            values += value
        }

        fun configure(lowTo: Output, highTo: Output) {
            this.lowTo = lowTo
            this.highTo = highTo
        }

        fun execute(): Boolean {
            val values = listOf(this.values.removeFirst(), this.values.removeFirst())

            val highValue = values.max()
            val lowValue = values.min()

            when (highTo) {
                is Output.Output -> outputs[highTo.id] = highValue
                is Output.Bot -> bots.getValue(highTo.id).addChip(highValue)
            }
            when (lowTo) {
                is Output.Output -> outputs[lowTo.id] = lowValue
                is Output.Bot -> bots.getValue(lowTo.id).addChip(lowValue)
            }
            return highValue == 61 && lowValue == 17
        }

    }

    private sealed interface Output {
        val id: Int

        class Output(override val id: Int) : Day10.Output
        class Bot(override val id: Int) : Day10.Output
    }


}