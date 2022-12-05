package aoc2022

import Input
import java.util.Stack
import java.util.regex.Matcher
import java.util.regex.Pattern

object Day5 {
    fun part1(input: Input) {
        val (stacks, commandMatcher) = readInput(input)

        while (commandMatcher.find()) {
            val count = commandMatcher.group(1).toInt()
            val fromStack = stacks[commandMatcher.group(2).toInt() - 1]
            val toStack = stacks[commandMatcher.group(3).toInt() - 1]

            repeat(count) {
                toStack.push(fromStack.pop())
            }
        }

        println(stacks.joinToString(separator = "") { it.peek() })
    }

    fun part2(input: Input) {
        val (stacks, commandMatcher) = readInput(input)

        while (commandMatcher.find()) {
            val count = commandMatcher.group(1).toInt()
            val fromStack = stacks[commandMatcher.group(2).toInt() - 1]
            val toStack = stacks[commandMatcher.group(3).toInt() - 1]

            val popped = mutableListOf<String>()
            repeat(count) {
                popped += fromStack.pop()
            }
            popped.reversed().forEach(toStack::push)
        }

        println(stacks.joinToString(separator = "") { it.peek() })
    }

    private fun readInput(input: Input): ParsedInput {
        val (stackConfigRaw, commands) = input.asText().split("\n\n")
        val stackConfig = stackConfigRaw.split('\n')
        val stackCount = stackConfig.last().split(' ').map { it.trim() }.count { it.isNotEmpty() }
        val stacks = MutableList<Stack<String>>(stackCount) { Stack() }
        stackConfig.dropLast(1)
            .forEach { line ->
                parseCrates(line).forEachIndexed { index, crate ->
                    if (crate.isNotEmpty()) {
                        stacks[index].add(0, crate)
                    }
                }
            }

        val commandMatcher = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)")
            .matcher(commands)
        return ParsedInput(stacks, commandMatcher)
    }

    private fun parseCrates(line: String): List<String> {
        if (!line.contains('[')) {
            return emptyList()
        }
        val count = (line.length + 1) / 4
        val result = ArrayList<String>(count)
        repeat(count) { i ->
            result += line[i * 4 + 1].toString().trim()
        }
        return result
    }

    private data class ParsedInput(
        val stacks: List<Stack<String>>,
        val commandMatcher: Matcher
    )
}