package aoc2022

import Input
import kotlin.math.max

object Day19 {

    fun part1(input: Input) {
        val blueprints = readBlueprints(input)
        val cache = mutableMapOf<State, Int>()
        var allBest = 0

        fun countGeodes(blueprint: Blueprint, state: State): Int {
            if (state.time == 24) {
                return state.geode
            }
            if (state.getPossibleBestGeodeCount(24) <= allBest) {
                return 0
            }
            val cachedValue = cache[state]
            if (cachedValue != null) {
                return cachedValue
            }
            val option1 = Robot.values().maxOf { robot ->
                if (state.canBuyRobot(blueprint, robot)) {
                    countGeodes(blueprint, state.buyRobot(blueprint, robot))
                } else {
                    0
                }
            }
            val option2 = countGeodes(blueprint, state.collect())
            val best = max(option1, option2)
            cache[state] = best
            allBest = max(allBest, best)
            return best
        }

        println(
            blueprints.sumOf { b ->
                cache.clear()
                allBest = 0
                val count = countGeodes(b, State())
                println("${b.id}: $count")
                b.id * count
            }
        )
    }

    fun part2(input: Input) {
        val blueprints = readBlueprints(input).take(3)
        val cache = mutableMapOf<State, Int>()
        var allBest = 0

        fun countGeodes(blueprint: Blueprint, state: State): Int {
            if (state.time == 32) {
                return state.geode
            }
            if (state.getPossibleBestGeodeCount(32) <= allBest) {
                return 0
            }
            val cachedValue = cache[state]
            if (cachedValue != null) {
                return cachedValue
            }
            val option1 = Robot.values().maxOf { robot ->
                if (state.canBuyRobot(blueprint, robot)) {
                    countGeodes(blueprint, state.buyRobot(blueprint, robot))
                } else {
                    0
                }
            }
            val option2 = countGeodes(blueprint, state.collect())
            val best = max(option1, option2)
            cache[state] = best
            allBest = max(allBest, best)
            return best
        }

        println(
            blueprints.fold(1) { acc, b ->
                cache.clear()
                allBest = 0
                val count = countGeodes(b, State())
                println("${b.id}: $count")
                acc * count
            }
        )
    }

    private fun readBlueprints(input: Input): List<Blueprint> =
        input.asLines()
            .map { line -> line.split(" ") }
            .map { split -> split.mapNotNull { (it.dropLastWhile { ch -> ch == ':' }).toIntOrNull() } }
            .map { values ->
                Blueprint(
                    values[0],
                    mapOf(
                        Robot.ORE to Cost(ore = values[1]),
                        Robot.CLAY to Cost(ore = values[2]),
                        Robot.OBSIDIAN to Cost(ore = values[3], clay = values[4]),
                        Robot.GEODE to Cost(ore = values[5], obsidian = values[6])
                    )
                )
            }

    private data class Blueprint(
        val id: Int,
        val robotCost: Map<Robot, Cost>
    )

    private data class Cost(
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0
    )

    private data class State(
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geode: Int = 0,
        val time: Int = 0,
        val robotCount: Map<Robot, Int> = mapOf(Robot.ORE to 1, Robot.CLAY to 0, Robot.OBSIDIAN to 0, Robot.GEODE to 0)
    ) {
        fun canBuyRobot(blueprint: Blueprint, robot: Robot): Boolean {
            val cost = blueprint.robotCost.getValue(robot)
            return cost.ore <= ore && cost.clay <= clay && cost.obsidian <= obsidian
        }

        fun buyRobot(blueprint: Blueprint, robot: Robot): State {
            val cost = blueprint.robotCost.getValue(robot)
            return copy(
                ore = ore - cost.ore + robotCount.getValue(Robot.ORE),
                clay = clay - cost.clay + robotCount.getValue(Robot.CLAY),
                obsidian = obsidian - cost.obsidian + robotCount.getValue(Robot.OBSIDIAN),
                geode = geode + robotCount.getValue(Robot.GEODE),
                time = time + 1,
                robotCount = robotCount.toMutableMap().apply { put(robot, getValue(robot) + 1) }
            )
        }

        fun collect(): State {
            return copy(
                ore = ore + robotCount.getValue(Robot.ORE),
                clay = clay + robotCount.getValue(Robot.CLAY),
                obsidian = obsidian + robotCount.getValue(Robot.OBSIDIAN),
                geode = geode + robotCount.getValue(Robot.GEODE),
                time = time + 1
            )
        }

        fun getPossibleBestGeodeCount(maxTime: Int): Int {
            val remainingTime = maxTime - time
            var geode = geode
            var robotCount = robotCount.getValue(Robot.GEODE)
            repeat(remainingTime) {
                geode += robotCount
                robotCount++
            }
            return geode
        }
    }

    private enum class Robot {
        GEODE,
        OBSIDIAN,
        CLAY,
        ORE,
    }

}