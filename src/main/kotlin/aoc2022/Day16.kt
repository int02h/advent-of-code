package aoc2022

import Input
import java.util.regex.Pattern
import kotlin.math.max

object Day16 {

    fun part1(input: Input) {
        val valves = readInput(input)
        val cache = mutableMapOf<String, Int>()

        fun calculateMaxPressure(
            valveId: String,
            openedValves: List<String>,
            releasedPressure: Int,
            time: Int,
        ): Int {
            if (time == 30) {
                return releasedPressure
            }

            val cacheKey = "$valveId:${openedValves.joinToString(separator = ",")}:$time"
            if (cache.contains(cacheKey)) {
                return cache.getValue(cacheKey)
            }

            val valve = valves.getValue(valveId)
            val option1 = if (!openedValves.contains(valveId) && valve.flowRate > 0) {
                calculateMaxPressure(
                    valveId = valveId,
                    openedValves = openedValves + valveId,
                    releasedPressure = releasedPressure + openedValves.sumOf { valves.getValue(it).flowRate },
                    time = time + 1
                )
            } else {
                0
            }
            val option2 = valve.connected.maxOf { v ->
                calculateMaxPressure(
                    valveId = v.id,
                    openedValves = openedValves,
                    releasedPressure = releasedPressure + openedValves.sumOf { valves.getValue(it).flowRate },
                    time = time + 1
                )
            }
            val maxPressure = max(option1, option2)
            cache[cacheKey] = maxPressure
            return maxPressure
        }

        println(calculateMaxPressure("AA", emptyList(), 0, 0))
    }

    fun part2(input: Input) {
        val valves = readInput(input)

        valves.forEach { (_, valve) ->
            valve.connected.sortByDescending { it.flowRate }
        }

        val cache = Array(30) { mutableMapOf<String, MutableMap<String, Int>>() }
        val nonZeroFlowValve = valves.values.filter { it.flowRate > 0 }
        val nonZeroFlowValveCount = nonZeroFlowValve.size
        val maxTotalFlowRate = nonZeroFlowValve.sumOf { it.flowRate }
        var currentBest = 11 * maxTotalFlowRate
        var lastPrintTime = 0L

        fun calculateMaxPressure(
            elfValveId: String,
            elephantValveId: String,
            openedValves: List<String>,
            releasedPressure: Int,
            time: Int,
        ): Int {
            if (System.currentTimeMillis() > lastPrintTime + 10000) {
                lastPrintTime = System.currentTimeMillis()
                print("\rcurrentBest=$currentBest            ")
            }
            if (time == 26) {
                return releasedPressure
            }
            if (openedValves.size == nonZeroFlowValveCount) {
                return releasedPressure + (26 - time) * openedValves.sumOf { valves.getValue(it).flowRate }
            }
            if (releasedPressure + (26 - time) * maxTotalFlowRate < currentBest) {
                return 0
            }

            val cachedValue =
                cache[time]["$elfValveId:$elephantValveId"]?.get(openedValves.joinToString(separator = ","))
            if (cachedValue != null) {
                return cachedValue
            }

            val elfValve = valves.getValue(elfValveId)
            val elephantValve = valves.getValue(elephantValveId)

            // both open
            val option1 = if (
                elfValveId != elephantValveId &&
                !openedValves.contains(elfValveId) && elfValve.flowRate > 0 &&
                !openedValves.contains(elephantValveId) && elephantValve.flowRate > 0
            ) {
                calculateMaxPressure(
                    elfValveId = elfValveId,
                    elephantValveId = elephantValveId,
                    openedValves = openedValves + elfValveId + elephantValveId,
                    releasedPressure = releasedPressure + openedValves.sumOf { valves.getValue(it).flowRate },
                    time = time + 1
                )
            } else {
                0
            }

            // elf opens, elephant moves
            val option2 = if (!openedValves.contains(elfValveId) && elfValve.flowRate > 0) {
                elephantValve.connected.maxOf { elephantConnection ->
                    if (elephantConnection.id != elfValveId) {
                        calculateMaxPressure(
                            elfValveId = elfValveId,
                            elephantValveId = elephantConnection.id,
                            openedValves = openedValves + elfValveId,
                            releasedPressure = releasedPressure + openedValves.sumOf { valves.getValue(it).flowRate },
                            time = time + 1
                        )
                    } else {
                        0
                    }
                }
            } else {
                0
            }

            // elf moves, elephant opens
            val option3 = if (!openedValves.contains(elephantValveId) && elephantValve.flowRate > 0) {
                elfValve.connected.maxOf { elfConnection ->
                    if (elfConnection.id != elephantValveId) {
                        calculateMaxPressure(
                            elfValveId = elfConnection.id,
                            elephantValveId = elephantValveId,
                            openedValves = openedValves + elephantValveId,
                            releasedPressure = releasedPressure + openedValves.sumOf { valves.getValue(it).flowRate },
                            time = time + 1
                        )
                    } else {
                        0
                    }
                }
            } else {
                0
            }

            // both move
            val option4 = elfValve.connected.maxOf { elfConnection ->
                elephantValve.connected.maxOf { elephantConnection ->
                    if (elfConnection.id != elephantValveId && elephantConnection.id != elfValveId) {
                        calculateMaxPressure(
                            elfValveId = elfConnection.id,
                            elephantValveId = elephantConnection.id,
                            openedValves = openedValves,
                            releasedPressure = releasedPressure + openedValves.sumOf { valves.getValue(it).flowRate },
                            time = time + 1
                        )
                    } else {
                        0
                    }
                }
            }

            val oldMaxValue =
                cache[time]["$elfValveId:$elephantValveId"]?.get(openedValves.joinToString(separator = ",")) ?: 0
            val newMaxValue = listOf(option1, option2, option3, option4, oldMaxValue).max()
            cache[time].getOrPut("$elfValveId:$elephantValveId") { mutableMapOf() }[openedValves.joinToString(separator = ",")] =
                newMaxValue
            currentBest = max(currentBest, newMaxValue)
            return newMaxValue
        }

        println(calculateMaxPressure("AA", "AA", emptyList(), 0, 0))
    }

    private fun readInput(input: Input): Map<String, Valve> {
        val valves = mutableMapOf<String, Valve>()
        val matcher = Pattern.compile("Valve (.+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)\n?")
            .matcher(input.asText())
        while (matcher.find()) {
            val valve = valves.getOrPut(matcher.group(1)) { Valve(matcher.group(1)) }
            valve.flowRate = matcher.group(2).toInt()
            matcher.group(3).split(", ").forEach { connection ->
                val toValve = valves.getOrPut(connection) { Valve(connection) }
                valve.connectTo(toValve)
            }
        }
        return valves
    }

    private class Valve(val id: String) {
        val connected = mutableListOf<Valve>()

        var flowRate: Int = -1

        fun connectTo(valve: Valve) {
            if (connected.find { it.id == valve.id } == null) {
                connected += valve
            }
        }

        override fun toString(): String = "$id ($flowRate)"
    }
}