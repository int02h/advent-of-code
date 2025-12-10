package aoc2025

import AocDay2
import Input
import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status


class Day10 : AocDay2 {

    private lateinit var manuals: List<Manual>

    override fun readInput(input: Input) {
        manuals = input.asLines().map { line ->
            val parts = line.split(" ")
            val indicatorDiagram = parts[0].substring(1, parts[0].lastIndex)
            val buttonWiring = parts.subList(1, parts.lastIndex)
                .map { it.substring(1, it.lastIndex) }
                .map { wiring -> wiring.split(",").map { it.toInt() } }
                .sortedByDescending { it.size }
            val joltageRequirements = parts.last()
                .substring(1, parts.last().lastIndex)
                .split(",")
                .map { it.toInt() }

            Manual(indicatorDiagram, buttonWiring, joltageRequirements)
        }
    }

    override fun part1() {
        println(
            manuals.sumOf { m ->
                findSolution(
                    m,
                    State(m.indicatorDiagram.replace('#', '.')),
                    mutableMapOf()
                )
            }
        )
    }

    override fun part2() {
        println(manuals.sumOf { m -> solveEquations(m) })
    }

    private fun findSolution(
        manual: Manual,
        state: State,
        cache: MutableMap<State, Int>,
    ): Int {
        if (state.pressCount > manual.buttonWiring.size) {
            return Int.MAX_VALUE
        }
        if (state.indicator == manual.indicatorDiagram) {
            return state.pressCount
        }
        if (cache.contains(state)) {
            return cache.getValue(state)
        }
        val results = manual.buttonWiring.map { w ->
            findSolution(manual, state.toggle(w), cache)
        }
        val best = results.min()
        cache[state] = best
        return best
    }

    private fun solveEquations(manual: Manual): Int {
        val ctx = Context()
        val opt = ctx.mkOptimize()
        val presses = ctx.mkIntConst("presses")

        val buttonVars = (0 until manual.buttonWiring.size)
            .map { ctx.mkIntConst("button$it") }

        val countersToButtons = mutableMapOf<Int, MutableList<IntExpr>>()

        manual.buttonWiring.forEachIndexed { i, wiring ->
            val buttonVar = buttonVars[i]
            for (w in wiring) {
                countersToButtons.getOrPut(w) { mutableListOf() } += buttonVar
            }
        }

        countersToButtons.forEach { index, buttons ->
            val targetValue = ctx.mkInt(manual.joltageRequirements[index])
            val sumOfButtonPresses = ctx.mkAdd(*buttons.toTypedArray())
            val equation = ctx.mkEq(targetValue, sumOfButtonPresses)
            opt.Add(equation)
        }

        val zero = ctx.mkInt(0)
        for (buttonVar in buttonVars) {
            val nonNegative = ctx.mkGe(buttonVar, zero)
            opt.Add(nonNegative)
        }

        val sumOfAllButtonVars = ctx.mkAdd(*buttonVars.toTypedArray())
        val totalPressesEq = ctx.mkEq(presses, sumOfAllButtonVars)
        opt.Add(totalPressesEq)

        opt.MkMinimize(presses)
        val status = opt.Check()
        if (status != Status.SATISFIABLE) {
            error(status)
        }

        val outputValue = opt.getModel().evaluate(presses, false) as IntNum
        return outputValue.int
    }

    private data class Manual(
        val indicatorDiagram: String,
        val buttonWiring: List<List<Int>>,
        val joltageRequirements: List<Int>,
    )

    private data class State(
        val indicator: String,
        val pressCount: Int = 0,
    ) {
        fun toggle(wiring: List<Int>): State {
            val arr = indicator.toCharArray()
            wiring.forEach { w ->
                arr[w] = if (arr[w] == '.') '#' else '.'
            }
            return State(
                arr.concatToString(),
                pressCount + 1,
            )
        }
    }
}
