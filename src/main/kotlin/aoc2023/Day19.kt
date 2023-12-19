package aoc2023

import AocDay
import Input
import kotlin.math.max
import kotlin.math.min

object Day19 : AocDay {

    override fun part1(input: Input) {
        val data = parse(input)
        val startRule = data.rules.getValue("in")
        val accepted = data.parts.filter { p ->
            var result: Boolean? = null
            var rule = startRule
            while (result == null) {
                when (val action = rule.getAction(p)) {
                    Action.Accept -> result = true
                    is Action.GoToRule -> rule = data.rules.getValue(action.name)
                    Action.Reject -> result = false
                }
            }
            result
        }
        println(accepted.sumOf { it.getRating() })
    }

    override fun part2(input: Input) {
        val data = parse(input)
        val startRule = data.rules.getValue("in")
        val chains = mutableListOf<List<Check>>()
        getAcceptChains(startRule, data.rules, emptyList(), chains)

        val xRanges = mutableListOf<IntRange>()
        val mRanges = mutableListOf<IntRange>()
        val aRanges = mutableListOf<IntRange>()
        val sRanges = mutableListOf<IntRange>()

        chains.forEach { chain ->
            val start = mutableMapOf<String, Int>()
            val end = mutableMapOf<String, Int>()
            chain.forEach { c ->
                when (c) {
                    is Check.Greater -> start[c.property] = max(c.value + 1, start[c.property] ?: 1)
                    is Check.Lower -> end[c.property] = min(c.value - 1, end[c.property] ?: 4000)
                    Check.Always -> error("Should never be here")
                }
            }

            xRanges += (start["x"] ?: 1)..(end["x"] ?: 4000)
            mRanges += (start["m"] ?: 1)..(end["m"] ?: 4000)
            aRanges += (start["a"] ?: 1)..(end["a"] ?: 4000)
            sRanges += (start["s"] ?: 1)..(end["s"] ?: 4000)
        }

        val n = xRanges.size
        var sum = 0L
        repeat(n) {
            sum += (xRanges[it].last - xRanges[it].first + 1L) *
                    (mRanges[it].last - mRanges[it].first + 1L) *
                    (aRanges[it].last - aRanges[it].first + 1L) *
                    (sRanges[it].last - sRanges[it].first + 1L)
        }
        println(sum)
    }

    private fun parse(input: Input): InputData {
        val (rulesRaw, partsRaw) = input.asText().split("\n\n")
        return InputData(
            rules = rulesRaw.split("\n").map(Rule::parse).associateBy { it.name },
            parts = partsRaw.split("\n").map(Part::parse),
        )
    }

    private fun getAcceptChains(
        rule: Rule,
        rules: Map<String, Rule>,
        currentChain: List<Check>,
        out: MutableList<List<Check>>
    ) {
        val reverted = mutableListOf<Check>()
        rule.conditions.forEachIndexed { index, c ->
            val checks = (if (index == 0) listOf(c.check) else reverted.toList() + c.check)
                .filter { it !is Check.Always }
            when (c.action) {
                Action.Accept -> out.add(currentChain + checks)
                is Action.GoToRule -> getAcceptChains(
                    rule = rules.getValue(c.action.name),
                    rules = rules,
                    currentChain = currentChain + checks,
                    out = out
                )
                Action.Reject -> Unit // do nothing
            }
            when (c.check) {
                is Check.Greater -> Check.Lower(c.check.property, c.check.value + 1)
                is Check.Lower -> Check.Greater(c.check.property, c.check.value - 1)
                Check.Always -> null
            }?.let(reverted::add)
        }
    }

    private class InputData(
        val rules: Map<String, Rule>,
        val parts: List<Part>
    )

    private class Rule(val name: String, val conditions: List<Condition>) {
        fun getAction(part: Part): Action {
            return conditions.first { c ->
                if (c.check is Check.Always) {
                    true
                } else {
                    c.check.check(part)
                }
            }.action
        }

        companion object {
            fun parse(value: String): Rule {
                val name = value.substring(0, value.indexOf('{'))
                val conditions = value.substring(value.indexOf('{') + 1, value.indexOf('}'))
                    .split(",")
                    .map(Condition::parse)
                return Rule(name, conditions)
            }
        }
    }

    private class Condition(val check: Check, val action: Action) {
        companion object {
            fun parse(value: String): Condition {
                val colonIndex = value.indexOf(':')
                if (colonIndex == -1) {
                    return Condition(Check.Always, Action.parse(value))
                }
                val (check, action) = value.split(":")
                return Condition(Check.parse(check), Action.parse(action))
            }
        }
    }

    private sealed class Check {
        abstract fun check(part: Part): Boolean

        class Greater(val property: String, val value: Int) : Check() {
            override fun check(part: Part): Boolean = part.getValue(property) > value
            override fun toString(): String = "$property>$value"
        }

        class Lower(val property: String, val value: Int) : Check() {
            override fun check(part: Part): Boolean = part.getValue(property) < value
            override fun toString(): String = "$property<$value"
        }

        object Always : Check() {
            override fun check(part: Part): Boolean = true
            override fun toString(): String = "always"
        }

        companion object {
            fun parse(check: String): Check {
                val property = check.takeWhile { it.isLetterOrDigit() }
                val op = check.substring(property.length)

                if (op.startsWith('>')) {
                    return Greater(property, op.substring(1).toInt())
                }
                if (op.startsWith('<')) {
                    return Lower(property, op.substring(1).toInt())
                }
                error("Impossible")
            }
        }
    }

    private sealed class Action {
        object Accept : Action()
        object Reject : Action()
        class GoToRule(val name: String) : Action()

        companion object {
            fun parse(value: String): Action =
                when (value) {
                    "A" -> Accept
                    "R" -> Reject
                    else -> GoToRule(value)
                }
        }
    }

    private class Part : HashMap<String, Int>() {
        fun getRating(): Int = values.sum()

        companion object {
            fun parse(value: String): Part = Part().apply {
                value.substring(1, value.lastIndex)
                    .split(",")
                    .forEach {
                        val (propName, propValue) = it.split("=")
                        set(propName, propValue.toInt())
                    }
            }
        }
    }

}