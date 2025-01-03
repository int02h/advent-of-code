package aoc2015

import AocDay2
import Input

class Day23 : AocDay2 {

    private lateinit var commands: List<Command>

    override fun readInput(input: Input) {
        fun register(name: String) = if (name == "a") Register.A else Register.B

        commands = input.asLines().map {
            when (it.substring(0, 3)) {
                "hlf" -> Command.Hlf(register(it.substring(4)))
                "tpl" -> Command.Tpl(register(it.substring(4)))
                "inc" -> Command.Inc(register(it.substring(4)))
                "jmp" -> Command.Jmp(it.substring(4).toInt())
                "jie" -> Command.Jie(register(it.substring(4, 5)), it.substring(7).toInt())
                "jio" -> Command.Jio(register(it.substring(4, 5)), it.substring(7).toInt())
                else -> error("Unknown command $it")
            }
        }
    }

    override fun part1() {
        val computer = Computer()
        computer.run(commands)
        println(computer.registers[Register.B])
    }

    override fun part2() {
        val computer = Computer()
        computer.registers[Register.A] = 1
        computer.run(commands)
        println(computer.registers[Register.B])
    }

    private class Computer {
        var ci = 0
        val registers = mutableMapOf<Register, Int>().withDefault { 0 }

        fun run(commands: List<Command>) {
            while (ci < commands.size) {
                commands[ci].execute(this)
            }
        }
    }

    private sealed interface Command {

        fun execute(computer: Computer)

        class Hlf(val r: Register) : Command {
            override fun execute(computer: Computer) {
                computer.registers[r] = computer.registers.getValue(r) / 2
                computer.ci++
            }
        }

        class Tpl(val r: Register) : Command {
            override fun execute(computer: Computer) {
                computer.registers[r] = computer.registers.getValue(r) * 3
                computer.ci++
            }
        }

        class Inc(val r: Register) : Command {
            override fun execute(computer: Computer) {
                computer.registers[r] = computer.registers.getValue(r) + 1
                computer.ci++
            }
        }

        class Jmp(val offset: Int) : Command {
            override fun execute(computer: Computer) {
                computer.ci += offset
            }
        }

        class Jie(val r: Register, val offset: Int) : Command {
            override fun execute(computer: Computer) {
                if (computer.registers.getValue(r) % 2 == 0) {
                    computer.ci += offset
                } else {
                    computer.ci++
                }
            }
        }

        class Jio(val r: Register, val offset: Int) : Command {
            override fun execute(computer: Computer) {
                if (computer.registers.getValue(r) == 1) {
                    computer.ci += offset
                } else {
                    computer.ci++
                }
            }
        }
    }

    private enum class Register { A, B }
}