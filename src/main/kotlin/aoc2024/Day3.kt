package aoc2024

import AocDay
import Input
import java.util.regex.Pattern

object Day3 : AocDay {

    override fun part1(input: Input) {
        val pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)")
        val matcher = pattern.matcher(input.asText().trim())
        var result = 0
        matcher.results().forEach { res ->
            val op1 = res.group(1).toInt()
            val op2 = res.group(2).toInt()
            result += op1 * op2
        }
        println(result)
    }

    override fun part2(input: Input) {
        val mulPattern = "mul\\((\\d{1,3}),(\\d{1,3})\\)"
        val enablePattern = "do\\(\\)"
        val disablePattern = "don't\\(\\)"
        val pattern = Pattern.compile("$mulPattern|$enablePattern|$disablePattern")
        val matcher = pattern.matcher(input.asText().trim())
        var result = 0
        var isEnabled = true
        matcher.results().forEach { res ->
            when(res.group()) {
                "do()" -> isEnabled = true
                "don't()" -> isEnabled = false
                else -> {
                    if (isEnabled) {
                        val op1 = res.group(1).toInt()
                        val op2 = res.group(2).toInt()
                        result += op1 * op2
                    }
                }
            }

        }
        println(result)
    }
}