package aoc2023

import AocDay
import Input

object Day20 : AocDay {

    override fun part1(input: Input) {
        val modules = parse(input)
        val broadcaster = modules.getValue("broadcaster")
        val machine = Machine(broadcaster)
        repeat(1000) {
            machine.pressButton()
        }
        println(machine.lowCounter * machine.highCounter)
    }

    override fun part2(input: Input) {
        val moduleNames = listOf("nx", "dj", "zp", "bz")
        val counts = moduleNames.map { targetName ->
            val modules = parse(input)
            val broadcaster = modules.getValue("broadcaster")
            val machine = Machine(broadcaster)
            var count: Long? = null
            machine.onPulseHandled = { pulse ->
                if (pulse.source.name == targetName && pulse.type == PulseType.LOW) {
                    count = machine.pressButtonCount
                }
            }
            while (count == null) {
                machine.pressButton()
            }
            count!!
        }
        fun gcd(a: Long, b: Long): Long {
            return if (b == 0L) a else gcd(b, a % b)
        }
        fun lcm(a: Long, b: Long): Long {
            return (a * b) / gcd(a, b)
        }
        println(counts.fold(1L) { acc, value -> lcm(acc, value) })
    }

    private class Machine(val broadcaster: Module) {
        private val pulseQueue = mutableListOf<Pulse>()
        var lowCounter = 0L
        var highCounter = 0L
        var pressButtonCount = 0L

        var onPulseHandled: ((Pulse) -> Unit)? = null

        fun pressButton() {
            pressButtonCount++
            lowCounter++
            broadcaster.outputs.forEach {
                pulseQueue.add(Pulse(PulseType.LOW, source = broadcaster, destination = it))
            }
            while (pulseQueue.isNotEmpty()) {
                val pulse = pulseQueue.removeFirst()
                when (pulse.type) {
                    PulseType.LOW -> lowCounter++
                    PulseType.HIGH -> highCounter++
                }
                val handleResult = pulse.destination.handle(pulse)
                onPulseHandled?.invoke(pulse)
                pulseQueue.addAll(handleResult)
            }
        }
    }

    private fun parse(input: Input): Map<String, Module> {
        val modules = mutableMapOf<String, Module>()

        fun getModuleByTypeAndName(typeAndName: String): Module {
            val (name, type) = when {
                typeAndName.startsWith('%') -> typeAndName.substring(1) to ModuleType.FLIP_FLOP
                typeAndName.startsWith('&') -> typeAndName.substring(1) to ModuleType.CONJUNCTION
                else -> typeAndName to ModuleType.BROADCASTER
            }
            val module = modules.getOrPut(name) { Module(name) }
            module.type = type
            return module
        }

        fun getModuleByName(name: String): Module = modules.getOrPut(name) { Module(name) }

        input.asLines().map { line ->
            val (name, connections) = line.split(" -> ")
            val module = getModuleByTypeAndName(name)
            connections.split(", ").forEach {
                val output = getModuleByName(it)
                module.outputs += output
                output.inputs += module
            }
        }

        return modules
    }

    private class Module(val name: String) {
        var type: ModuleType? = null
        val inputs = mutableListOf<Module>()
        val outputs = mutableListOf<Module>()

        var flipFlop = false
        val conjunctionMemory = mutableMapOf<Module, PulseType>()

        fun handle(pulse: Pulse): List<Pulse> = when (type) {
            ModuleType.BROADCASTER -> error("Should never happen")
            ModuleType.FLIP_FLOP -> {
                if (pulse.type == PulseType.LOW) {
                    flipFlop = !flipFlop
                    val pulseType = if (flipFlop) PulseType.HIGH else PulseType.LOW
                    buildList {
                        outputs.forEach {
                            add(Pulse(type = pulseType, source = this@Module, destination = it))
                        }
                    }
                } else {
                    emptyList()
                }
            }
            ModuleType.CONJUNCTION -> {
                conjunctionMemory[pulse.source] = pulse.type
                val pulseType = if (inputs.all { conjunctionMemory[it] == PulseType.HIGH }) {
                    PulseType.LOW
                } else {
                    PulseType.HIGH
                }
                buildList {
                    outputs.forEach {
                        add(Pulse(type = pulseType, source = this@Module, destination = it))
                    }
                }
            }
            null -> emptyList()
        }

        override fun toString(): String = "$name $type"
    }

    private enum class ModuleType {
        BROADCASTER,
        FLIP_FLOP,
        CONJUNCTION
    }

    private class Pulse(val type: PulseType, val source: Module, val destination: Module) {
        override fun toString(): String {
            return "${source.name} -${type.name.lowercase()}-> ${destination.name}"
        }
    }

    private enum class PulseType { LOW, HIGH }
}