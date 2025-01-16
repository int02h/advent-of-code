package aoc2019

import AocDay2
import Input
import java.util.PriorityQueue

class Day25 : AocDay2 {

    private lateinit var program: LongArray

    override fun readInput(input: Input) {
        program = input.asText().trim().split(",").map { it.trim().toLong() }.toLongArray()
    }

    override fun part1() {
        val initialCommands = runCommands(emptyList())
        val queue = PriorityQueue<QueueItem> { q1, q2 -> q2.commands.size - q1.commands.size }
        queue.addAll(initialCommands.map { QueueItem(it) })

        try {
            while (queue.isNotEmpty()) {
                val item = queue.remove()
                val nextPossibleCommands = runCommands(item.commands)
                nextPossibleCommands.forEach { nextCommands ->
                    queue += QueueItem(item.commands + nextCommands)
                }
            }
        } catch (e: ResultException) {
            println(e.message)
        }
    }

    override fun part2() {
    }

    private fun runCommands(commands: List<String>): List<List<String>> {
        for (i in 1..commands.lastIndex) {
            when (commands[i]) {
                "north" -> if (commands[i - 1] == "south") return emptyList()
                "south" -> if (commands[i - 1] == "north") return emptyList()
                "west" -> if (commands[i - 1] == "east") return emptyList()
                "east" -> if (commands[i - 1] == "west") return emptyList()
            }
        }

        val controller = Controller(commands)
        val computer = IntCodeComputer(program, controller.computerInput, controller)
        return try {
            computer.runAll()
            controller.nextPossibleCommands()
        } catch (e: StopException) {
            emptyList()
        } catch (e: ContinueException) {
            return controller.nextPossibleCommands()
        }
    }

    private class QueueItem(
        val commands: List<String>
    )

    private class StopException : Exception()
    private class ContinueException : Exception()
    private class ResultException(message: String) : Exception(message)

    private class Controller(commands: List<String>) : ArrayList<Long>() {
        private var buffer = ""
        private var readState = ReadState.NONE

        private val doors = mutableListOf<String>()
        private val items = mutableListOf<String>()
        private val locationIds = mutableMapOf<String, Int>()

        val commands = commands.toMutableList()

        val computerInput = mutableListOf<Long>()

        override fun add(element: Long): Boolean {
            val ch = element.toInt().toChar()
            if (ch == '\n') {
                handleLine(buffer)
                buffer = ""
            } else {
                buffer += ch
            }
            return true
        }

        fun nextPossibleCommands(): List<List<String>> {
            val result = mutableListOf<List<String>>()
            doors.forEach { d ->
                if (items.size > 1) {
                    error("Oops")
                }
                items.filter {
                    it != "infinite loop" &&
                            it != "molten lava" &&
                            it != "dark matter" &&
                            it != "escape pod" &&
                            it != "photons" &&
                            it != "giant electromagnet"
                }.forEach {
                    result += listOf("take $it", d)
                }
                result += listOf(d)
            }
            return result
        }

        private fun handleLine(line: String) {
            readState = when (readState) {
                ReadState.NONE -> when (line) {
                    "Doors here lead:" -> ReadState.DOORS
                    "Items here:" -> ReadState.ITEMS
                    "Command?" -> {
                        if (commands.isEmpty()) {
                            throw ContinueException()
                        }
                        val command = commands.removeFirst()
                        if (computerInput.isNotEmpty()) {
                            error("Input is not empty")
                        }
                        computerInput.addAll((command + "\n").map { it.code.toLong() })
                        ReadState.NONE
                    }
                    "" -> ReadState.NONE
                    else -> {
                        if (line.startsWith("== ")) {
                            doors.clear()
                            items.clear()
                            val locationId = line.substring(3, line.length - 3)
                            locationIds[locationId] = locationIds.getOrDefault(locationId, 0) + 1
                            if (locationIds.getValue(locationId) == 4) {
                                throw StopException()
                            }
                        } else if (
                            line == "A loud, robotic voice says \"Alert! Droids on this ship are heavier than the detected value!\" and you are ejected back to the checkpoint." ||
                            line == "A loud, robotic voice says \"Alert! Droids on this ship are lighter than the detected value!\" and you are ejected back to the checkpoint."
                        ) {
                            throw StopException()
                        } else {
                            if (line.contains("airlock")) {
                                throw ResultException(line)
                            }
                        }
                        ReadState.OTHER
                    }
                }
                ReadState.DOORS ->
                    if (line.isNotEmpty()) {
                        doors += line.substring(2)
                        readState
                    } else {
                        ReadState.NONE
                    }
                ReadState.ITEMS ->
                    if (line.isNotEmpty()) {
                        items += line.substring(2)
                        readState
                    } else {
                        ReadState.NONE
                    }
                ReadState.OTHER -> ReadState.NONE
            }
        }

        private enum class ReadState { NONE, DOORS, ITEMS, OTHER }
    }
}