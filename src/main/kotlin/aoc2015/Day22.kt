package aoc2015

import AocDay2
import Input

class Day22 : AocDay2 {

    private var bossHp = 0
    private var bossDamage = 0
    private var isPart2 = false

    private val cache = mutableMapOf<State, Int>()

    override fun readInput(input: Input) {
        val (hp, damage) = input.asLines().map { line -> line.split(": ")[1].toInt() }
        bossHp = hp
        bossDamage = damage
    }

    override fun part1() {
        println(play(State(bossHp = bossHp)))
    }

    override fun part2() {
        isPart2 = true
        println(play(State(bossHp = bossHp)))
    }

    private fun play(inputState: State): Int {
        if (cache.containsKey(inputState)) {
            return cache.getValue(inputState)
        }
        var state = inputState.copy(isPlayerTurn = !inputState.isPlayerTurn)
        if (isPart2 && state.isPlayerTurn) {
            state = state.copy(playerHp = state.playerHp - 1)
            if (state.playerHp <= 0) {
                return Int.MAX_VALUE
            }
        }
        val effects = inputState.effects.toMutableMap()
        var shield = 0
        inputState.effects.forEach { (effect, timer) ->
            when (effect) {
                Effect.SHIELD -> shield = 7
                Effect.POISON -> state = state.copy(bossHp = state.bossHp - 3)
                Effect.RECHARGE -> state = state.copy(playerMana = state.playerMana + 101)
            }
            if (timer > 1) {
                effects[effect] = timer - 1
            } else {
                effects.remove(effect)
            }
        }
        if (state.bossHp <= 0) {
            return state.spentMana
        }
        if (state.playerHp <= 0 || state.playerMana < 53) {
            return Int.MAX_VALUE
        }
        state = state.copy(effects = effects)

        if (state.isPlayerTurn) {
            val spells = mutableListOf<Int>()
            if (state.playerMana >= 53) { // can cast Magic Missile
                spells += play(
                    state.copy(
                        bossHp = state.bossHp - 4,
                        playerMana = state.playerMana - 53,
                        spentMana = state.spentMana + 53
                    )
                )
            }
            if (state.playerMana >= 73) { // can cast Drain
                spells += play(
                    state.copy(
                        bossHp = state.bossHp - 2,
                        playerHp = state.playerHp + 2,
                        playerMana = state.playerMana - 73,
                        spentMana = state.spentMana + 73
                    )
                )
            }
            if (state.playerMana >= 113 && !state.effects.containsKey(Effect.SHIELD)) { // can cast Shield
                spells += play(
                    state.copy(
                        effects = state.effects + (Effect.SHIELD to 6),
                        playerMana = state.playerMana - 113,
                        spentMana = state.spentMana + 113
                    )
                )
            }
            if (state.playerMana >= 173 && !state.effects.containsKey(Effect.POISON)) { // can cast Poison
                spells += play(
                    state.copy(
                        effects = state.effects + (Effect.POISON to 6),
                        playerMana = state.playerMana - 173,
                        spentMana = state.spentMana + 173
                    )
                )
            }
            if (state.playerMana >= 229 && !state.effects.containsKey(Effect.RECHARGE)) { // can cast Recharge
                spells += play(
                    state.copy(
                        effects = state.effects + (Effect.RECHARGE to 5),
                        playerMana = state.playerMana - 229,
                        spentMana = state.spentMana + 229
                    )
                )
            }
            val best = spells.min()
            cache[inputState] = best
            return best
        } else {
            return play(state.copy(playerHp = state.playerHp - (bossDamage - shield)))
        }
    }

    private data class State(
        val bossHp: Int,
        val playerHp: Int = PLAYER_HP,
        val playerMana: Int = PLAYER_MANA,
        val spentMana: Int = 0,
        val effects: Map<Effect, Int> = emptyMap(),
        val isPlayerTurn: Boolean = false,
    )

    private enum class Effect {
        SHIELD,
        POISON,
        RECHARGE
    }

    private companion object {
        const val PLAYER_HP = 50
        const val PLAYER_MANA = 500
    }

}