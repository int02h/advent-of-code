package aoc2022

import Input

object Day10 {

    fun part1(input: Input) {
        val sliceCycles = listOf(20, 60, 100, 140, 180, 220)
        var result = 0
        val commands = input.asLines().map(Command::parse)
        val cpu = CPU(commands)
        while (cpu.tick()) {
            if (cpu.cycles in sliceCycles) {
                result += cpu.x * cpu.cycles
            }
        }
        println(result)
    }

    fun part2(input: Input) {
        val commands = input.asLines().map(Command::parse)
        val cpu = CPU(commands)
        val display = Array(6) { Array(40) { '.' } }
        var row = 0
        var col = 0
        do {
            val spriteRange = (cpu.x - 1)..(cpu.x + 1)
            if (col in spriteRange) {
                display[row][col] = '#'
            }
            col++
            if (col > display[row].lastIndex) {
                row++
                col = 0
            }
        } while (cpu.tick())

        display.forEach { println(it.joinToString(separator = "")) }
    }

    private class CPU(val commands: List<Command>) {
        var x = 1
        var cycles = 1
        var commandIndex = 0
        var nextCommandAt = commands.first().cycleCount

        fun tick(): Boolean {
            if (cycles == nextCommandAt) {
                commands[commandIndex++].execute(this)
                if (commandIndex > commands.lastIndex) {
                    return false
                }
                nextCommandAt += commands[commandIndex].cycleCount
            }
            cycles++
            return true
        }
    }

    private sealed class Command(val cycleCount: Int) {

        abstract fun execute(cpu: CPU)

        object Noop : Command(1) {
            override fun execute(cpu: CPU) {
                // do nothing
            }
        }

        class AddX(val value: Int) : Command(2) {
            override fun execute(cpu: CPU) {
                cpu.x += value
            }
        }

        companion object {
            fun parse(value: String): Command =
                if (value == "noop") {
                    Noop
                } else {
                    AddX(value.split(' ')[1].toInt())
                }
        }

    }
}