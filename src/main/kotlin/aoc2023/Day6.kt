package aoc2023

import AocDay
import Input

object Day6 : AocDay {

    override fun part1(input: Input) {
        val races = parse1(input)

        val counts = races.map { r ->
            var count = 0
            for (t in 1 until r.time) {
                val distance = runBoat(t, r.time)
                if (distance > r.distance) {
                    count++
                }
            }
            count
        }
        println(counts.fold(1) { acc, c -> acc * c })
    }

    override fun part2(input: Input) {
        val race = parse2(input)

        var start = 0L
        var distance = 0L
        while (distance <= race.distance) {
            start++
            distance = runBoat(start, race.time)
        }

        var end = race.time
        distance = 0
        while (distance <= race.distance) {
            end--
            distance = runBoat(end, race.time)
        }

        println(end - start + 1)
    }

    private fun parse1(input: Input): List<Race> {
        val (time, distance) = input.asLines().map { line ->
            line.split(":")[1]
                .split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toLong() }
        }
        return time.indices.map { Race(time[it], distance[it]) }
    }

    private fun parse2(input: Input): Race {
        val (time, distance) = input.asLines().map { line ->
            line.split(":")[1]
                .replace(" ", "")
                .toLong()
        }
        return Race(time, distance)
    }

    private fun runBoat(holdTime: Long, totalTime: Long): Long {
        val speed = holdTime
        val driveTime = totalTime - holdTime
        return driveTime * speed
    }

    private data class Race(val time: Long, val distance: Long)
}