package aoc2015

import AocDay
import Input
import kotlin.math.min

object Day14 : AocDay {

    override fun part1(input: Input) {
        val reindeers = readReindeers(input)
        println(reindeers.maxOf { r -> r.getDistance(2503) })
    }

    override fun part2(input: Input) {
        val reindeers = readReindeers(input)
        repeat(2503) {
            reindeers.forEach { it.tick() }
            val max = reindeers.maxOf { it.currentDistance }
            reindeers.filter { it.currentDistance == max }.forEach { it.score++ }
        }
        println(reindeers.maxOf { r -> r.score })
    }

    private fun readReindeers(input: Input): List<Reindeer> =
        input.asLines()
            .map { it.split(' ') }
            .map {
                Reindeer(
                    speed = it[3].toInt(),
                    flyTime = it[6].toInt(),
                    restTime = it[it.lastIndex - 1].toInt(),
                )
            }

    private class Reindeer(val speed: Int, val flyTime: Int, val restTime: Int) {

        var currentDistance = 0
        var isFlying = true
        var remainingTime = flyTime
        var score = 0

        fun getDistance(time: Int): Int {
            val wholeCycleTime = flyTime + restTime
            val wholeCycleCount = time / wholeCycleTime
            val remainingTime = (time - wholeCycleCount * wholeCycleTime)
            return wholeCycleCount * flyTime * speed + min(remainingTime, flyTime) * speed
        }

        fun tick() {
            if (isFlying) {
                currentDistance += speed
                if (--remainingTime == 0) {
                    remainingTime = restTime
                    isFlying = false
                }
            } else {
                if (--remainingTime == 0) {
                    remainingTime = flyTime
                    isFlying = true
                }
            }
        }

    }

}