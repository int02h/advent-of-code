package aoc2015

import AocDay
import Input
import kotlin.math.max

object Day15 : AocDay {

    override fun part1(input: Input) {
        findBestRecipe(input)
    }

    override fun part2(input: Input) {
        findBestRecipe(input, expectedCalories = 500)
    }

    private fun findBestRecipe(input: Input, expectedCalories: Long? = null) {
        val ingredients = readIngredients(input)
        val teaspoons = IntArray(ingredients.size) { 1 }
        val max = 100 - (teaspoons.size - 1)
        teaspoons[teaspoons.lastIndex] = max

        var bestScore = 0L
        while (teaspoons[teaspoons.lastIndex - 1] != max) {
            val capacityScore = ingredients.foldIndexed(0L) { index, acc, ingredient ->
                acc + ingredient.capacity * teaspoons[index]
            }
            val durabilityScore = ingredients.foldIndexed(0L) { index, acc, ingredient ->
                acc + ingredient.durability * teaspoons[index]
            }
            val flavorScore = ingredients.foldIndexed(0L) { index, acc, ingredient ->
                acc + ingredient.flavor * teaspoons[index]
            }
            val textureScore = ingredients.foldIndexed(0L) { index, acc, ingredient ->
                acc + ingredient.texture * teaspoons[index]
            }
            val caloriesScore = ingredients.foldIndexed(0L) { index, acc, ingredient ->
                acc + ingredient.calories * teaspoons[index]
            }
            if (capacityScore > 0 && durabilityScore > 0 && flavorScore > 0 && textureScore > 0) {
                if (expectedCalories == null || expectedCalories == caloriesScore) {
                    val totalScore = capacityScore * durabilityScore * flavorScore * textureScore
                    bestScore = max(bestScore, totalScore)
                }
            }
            nextTeaspoons(teaspoons)
        }
        println(bestScore)
    }

    private fun nextTeaspoons(teaspoons: IntArray) {
        var index = 0
        val max = 100 - (teaspoons.size - 1)
        while (index < teaspoons.lastIndex) {
            teaspoons[index]++
            if (teaspoons[index] > max) {
                teaspoons[index] = 1
                index++
            } else {
                break
            }
        }
        teaspoons[teaspoons.lastIndex] = 100 - teaspoons.dropLast(1).sum()
    }

    private fun readIngredients(input: Input): List<Ingredient> =
        input.asLines()
            .map { it.split(": ") }
            .map { (_, properties) -> properties.split(", ") }
            .map { properties -> properties.map { p -> p.split(' ')[1] } }
            .map { values -> values.map { it.toInt() } }
            .map {
                Ingredient(it[0], it[1], it[2], it[3], it[4])
            }

    private data class Ingredient(
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int,
    )

}