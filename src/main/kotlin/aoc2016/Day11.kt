package aoc2016

import AocDay2
import Input
import java.util.PriorityQueue
import kotlin.math.min

class Day11 : AocDay2 {

    override fun readInput(input: Input) = Unit

    private var currentBest = Int.MAX_VALUE

    override fun part1() {
        val state = State(
            listOf(
                setOf(
                    Component.Generator("thulium"),
                    Component.Microchip("thulium"),
                    Component.Generator("plutonium"),
                    Component.Generator("strontium")
                ),
                setOf(
                    Component.Microchip("plutonium"), Component.Microchip("strontium")
                ),
                setOf(
                    Component.Generator("promethium"),
                    Component.Microchip("promethium"),
                    Component.Generator("ruthenium"),
                    Component.Microchip("ruthenium")
                ),
                emptySet()
            )
        )
        val exampleState = State(
            listOf(
                setOf(
                    Component.Microchip("hydrogen"),
                    Component.Microchip("lithium"),
                ),
                setOf(
                    Component.Generator("hydrogen")
                ),
                setOf(
                    Component.Generator("lithium"),
                ),
                emptySet()
            )
        )
        println(
            state.floors[0].minOf { component ->
                dp(state.take(component))
            }
        )
    }

    override fun part2() {
        val state = State(
            listOf(
                setOf(
                    Component.Generator("thulium"),
                    Component.Microchip("thulium"),
                    Component.Generator("plutonium"),
                    Component.Generator("strontium"),
                    Component.Generator("elerium"),
                    Component.Microchip("elerium"),
                    Component.Generator("dilithium"),
                    Component.Microchip("dilithium"),
                ),
                setOf(
                    Component.Microchip("plutonium"), Component.Microchip("strontium")
                ),
                setOf(
                    Component.Generator("promethium"),
                    Component.Microchip("promethium"),
                    Component.Generator("ruthenium"),
                    Component.Microchip("ruthenium")
                ),
                emptySet()
            )
        )
        currentBest = 100
        println(
            state.floors[0].minOf { component ->
                val intermediateResult = dp(state.take(component), isPart2 = true)
                currentBest = min(currentBest, intermediateResult)
                println(intermediateResult)
                intermediateResult
            }
        )
    }

    private fun dp(initialState: State, isPart2: Boolean = false): Int {
        val visited = mutableMapOf<String, Int>()
        val queue = PriorityQueue<State> { p1, p2 -> p1.steps - p2.steps }
        queue.add(initialState)

        while (queue.isNotEmpty()) {
            val state = queue.remove()
            if (isPart2 && state.steps >= currentBest) continue
            val v = visited[state.asVisitedState()]
            if (v != null && v <= state.steps) {
                continue
            }
            visited[state.asVisitedState()] = state.steps
            if (state.isDone()) {
                return state.steps
            }
            if (state.bag.size < 2) {
                state.floors[state.elevatorAt].forEach { c ->
                    queue.add(state.take(c))
                }
            }
            val firstNonEmptyFloor = if (isPart2) -1 else state.floors.indexOfFirst { it.isNotEmpty() }
            if (state.elevatorAt > firstNonEmptyFloor) {
                state.bag.forEach { c ->
                    queue.add(state.leave(c))
                }
            }
            if (!(state.bag.size == 1 && state.elevatorAt == 2)) {
                state.move(1).let { ns ->
                    if (ns.isValid()) {
                        queue.add(ns)
                    }
                }
            }
            if (state.elevatorAt > firstNonEmptyFloor) {
                state.move(-1).let { ns ->
                    if (ns.isValid()) {
                        queue.add(ns)
                    }
                }
            }
        }
        return Int.MAX_VALUE
    }

    private sealed interface Component {
        val material: String

        data class Generator(override val material: String) : Component
        data class Microchip(override val material: String) : Component
    }

    private data class State(
        val floors: List<Set<Component>>,
        val bag: Set<Component> = emptySet(),
        val elevatorAt: Int = 0,
        val steps: Int = 0
    ) {
        fun isDone(): Boolean = floors[0].isEmpty() && floors[1].isEmpty() && floors[2].isEmpty() && elevatorAt == 3

        fun asVisitedState(): String = buildString {
            floors.forEach { f ->
                append("[")
                f.map { it.material }.sorted().forEach(::append)
                append("]")
            }
            append(";")
            bag.map { it.material }.sorted().forEach(::append)
            append(';').append(elevatorAt)
        }

        fun isValid(): Boolean {
            if (elevatorAt < 0 || elevatorAt >= floors.size) return false
            if (bag.size != 1 && bag.size != 2) return false
            if (!(bag + floors[elevatorAt]).isValid()) return false
            for (i in floors.indices) {
                if (i == elevatorAt) continue
                if (floors[i].isNotEmpty() && !floors[i].isValid()) return false
            }
            return true
        }

        fun take(component: Component): State {
            return State(
                floors = floors.mapIndexed { floor, components ->
                    if (floor == elevatorAt) components - component else components
                },
                bag = bag + component,
                elevatorAt = elevatorAt,
                steps = steps
            )
        }

        fun leave(component: Component): State {
            return State(
                floors = floors.mapIndexed { floor, components ->
                    if (floor == elevatorAt) components + component else components
                },
                bag = bag - component,
                elevatorAt = elevatorAt,
                steps = steps
            )
        }

        fun move(direction: Int): State {
            return State(
                floors = floors,
                bag = bag,
                elevatorAt = elevatorAt + direction,
                steps = steps + 1
            )
        }

        private fun Set<Component>.isValid(): Boolean {
            generatorSet.clear()
            microchipSet.clear()
            for (c in this) {
                if (c is Component.Generator) {
                    generatorSet += c.material
                } else {
                    microchipSet += c.material
                }
            }
            if (generatorSet.isEmpty() || microchipSet.isEmpty()) return true
            return generatorSet.containsAll(microchipSet)
        }

        private companion object {
            val generatorSet = mutableSetOf<String>()
            val microchipSet = mutableSetOf<String>()
        }
    }
}

