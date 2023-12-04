package aoc2023

import AocDay
import Input
import kotlin.math.max

object Day2 : AocDay {

    override fun part1(input: Input) {
        val games = parse(input)
        val cubes = mapOf("red" to 12, "green" to 13, "blue" to 14)
        val possible = games.filter { g ->
            g.sets.all { s ->
                s.pairs.all { (cube, count) ->
                    val result = cubes.getValue(cube) - count
                    result >= 0
                }
            }
        }
        println(possible.sumOf { it.id })
    }

    override fun part2(input: Input) {
        val games = parse(input)
        val sum = games.sumOf { g ->
            val cubes = mutableMapOf<String, Int>()
            g.sets.forEach { s ->
                s.pairs.forEach { (cube, count) ->
                    cubes[cube] = max(cubes.getOrDefault(cube, 0), count)
                }
            }
            cubes.values.fold(1L) { acc, value -> acc * value }
        }
        println(sum)
    }

    private fun parse(input: Input): List<Game> =
        input.asLines().map { line ->
            val (id, sets) = line.split(": ")
            Game(
                id = id.split(" ")[1].toInt(),
                sets = sets.split("; ").map { s ->
                    GameSet(
                        s.split(", ").associate { pair ->
                            val (count, cube) = pair.split(" ")
                            cube to count.toInt()
                        }
                    )
                })
        }

    private class Game(val id: Int, val sets: List<GameSet>) {

    }

    private class GameSet(val pairs: Map<String, Int>)
}