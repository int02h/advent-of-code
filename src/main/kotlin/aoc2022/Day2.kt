package aoc2022

import Input

object Day2 {
    fun part1(input: Input) {
        input.asLines()
            .map { it.split(" ") }
            .map { (opponent, me) -> Shape.opponent(opponent) to Shape.me(me) }
            .sumOf { (opponent, me) -> me.playWith(opponent) }
            .also(::println)
    }

    fun part2(input: Input) {
        input.asLines()
            .map { it.split(" ") }
            .map { (opponent, outcome) -> Shape.opponent(opponent) to Outcome.from(outcome) }
            .map { (opponent, outcome) -> opponent to outcome.findMyShapeFor(opponent) }
            .sumOf { (opponent, me) -> me.playWith(opponent) }
            .also(::println)
    }

    private enum class Shape(val value: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        fun playWith(shape: Shape): Int {
            val outcome = when (this) {
                ROCK -> when (shape) {
                    ROCK -> Outcome.DRAW
                    PAPER -> Outcome.LOSS
                    SCISSORS -> Outcome.WIN
                }
                PAPER -> when (shape) {
                    ROCK -> Outcome.WIN
                    PAPER -> Outcome.DRAW
                    SCISSORS -> Outcome.LOSS
                }
                SCISSORS -> when (shape) {
                    ROCK -> Outcome.LOSS
                    PAPER -> Outcome.WIN
                    SCISSORS -> Outcome.DRAW
                }
            }
            return this.value + outcome.value
        }

        companion object {
            fun opponent(value: String): Shape =
                when (value) {
                    "A" -> ROCK
                    "B" -> PAPER
                    "C" -> SCISSORS
                    else -> error("Unknown shape $value")
                }

            fun me(value: String): Shape =
                when (value) {
                    "X" -> ROCK
                    "Y" -> PAPER
                    "Z" -> SCISSORS
                    else -> error("Unknown shape $value")
                }
        }
    }

    private enum class Outcome(val value: Int) {
        WIN(6),
        LOSS(0),
        DRAW(3);

        fun findMyShapeFor(opponent: Shape) = when (this) {
            WIN -> when (opponent) {
                Shape.ROCK -> Shape.PAPER
                Shape.PAPER -> Shape.SCISSORS
                Shape.SCISSORS -> Shape.ROCK
            }
            LOSS -> when (opponent) {
                Shape.ROCK -> Shape.SCISSORS
                Shape.PAPER -> Shape.ROCK
                Shape.SCISSORS -> Shape.PAPER
            }
            DRAW -> opponent
        }

        companion object {
            fun from(value: String): Outcome =
                when (value) {
                    "X" -> LOSS
                    "Y" -> DRAW
                    "Z" -> WIN
                    else -> error("Unknown outcome $value")
                }
        }
    }
}