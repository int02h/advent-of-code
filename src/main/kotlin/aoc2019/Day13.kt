package aoc2019

import AocDay
import Input
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.math.sign

object Day13 : AocDay {
    override fun part1(input: Input) {
        val output = mutableListOf<Long>()
        val computer = IntCodeComputer(
            program = readProgram(input),
            input = mutableListOf(),
            output = output
        )

        val blockPositions = mutableSetOf<Position>()
        while (true) {
            computer.runUntilOutput(3)
            if (output.isEmpty()) {
                break
            }
            val x = output.removeFirst()
            val y = output.removeFirst()
            val tileId = output.removeFirst()
            if (tileId == 2L) {
                blockPositions.add(Position(x, y))
            } else {
                blockPositions.remove(Position(x, y))
            }
        }

        println(blockPositions.size)
    }

    override fun part2(input: Input) {
        val program = readProgram(input)
        program[0] = 2

        val screen = mutableMapOf<Position, Long>()
        val programInput = mutableListOf<Long>()
        val programOutput = mutableListOf<Long>()
        val computer = IntCodeComputer(program, programInput, programOutput)
        computer.runUntilInput()
        renderGame(screen, programOutput)

        var block = getNextBlockToBreak(screen)
        while (block != null) {
            computer.overriddenWriteValues[388] = block.x - 1 // ball x-position
            computer.overriddenWriteValues[389] = block.y - 1 // ball y-position
            computer.overriddenWriteValues[390] = 1 // ball x-vector
            computer.overriddenWriteValues[391] = 1 // ball y-vector
            programInput.add(0L)
            computer.executeNext()
            computer.runUntilInput()
            renderGame(screen, programOutput)
            block = getNextBlockToBreak(screen)
        }
        println(screen[Position(-1, 0)])

        Game(program).play()
    }

    private fun getNextBlockToBreak(screen: MutableMap<Position, Long>): Position? {
        val blocks = screen.filter { it.value == 2L }.map { it.key }
        if (blocks.isEmpty()) {
            return null
        }
        val firstRow = blocks.minOf { it.y }
        return blocks.filter { it.y == firstRow }.minByOrNull { it.x }
    }

    private fun renderGame(screen: MutableMap<Position, Long>, programOutput: MutableList<Long>) {
        while (programOutput.isNotEmpty()) {
            val tileX = programOutput.removeFirst()
            val tileY = programOutput.removeFirst()
            val tileId = programOutput.removeFirst()
            screen[Position(tileX, tileY)] = tileId
        }
    }

    private fun readProgram(input: Input): LongArray =
        input.asText().trim().split(",").map { it.trim().toLong() }.toLongArray()

    private data class Position(val x: Long, val y: Long) {
        constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
    }

    private class Game(program: LongArray) : JPanel() {
        private val frame = JFrame()
        private val widthInCells = 44
        private val heightInCells = 20
        private val cellSize = 24

        private lateinit var ballPosition: Position
        private lateinit var paddlePosition: Position

        val input = mutableListOf<Long>()
        val output = mutableListOf<Long>()
        val computer = IntCodeComputer(program, input, output)

        private val data = mutableMapOf<Position, Long>()
        var score = 0L

        init {
            preferredSize = Dimension(widthInCells * cellSize, heightInCells * cellSize)

            frame.add(this)
            frame.pack()
            doLayout()
            frame.isResizable = true
            frame.isVisible = true
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

            computer.runUntilInput()
        }

        fun play() {
            computer.runUntilInput()
            renderGame()

            while (!computer.isHalted) {
                input.add((ballPosition.x - paddlePosition.x).sign.toLong())
                computer.executeNext()
                computer.runUntilInput()
                renderGame()
            }
        }

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            for (row in 0 until heightInCells) {
                for (col in 0 until widthInCells) {
                    val x = col * cellSize
                    val y = row * cellSize
                    when (data[Position(col, row)]) {
                        null, 0L -> g.color = Color.WHITE
                        1L -> g.color = Color.BLACK
                        2L -> g.color = Color.GRAY
                        3L -> g.color = Color.BLACK
                        4L -> g.color = Color.RED
                    }
                    g.fillRect(x, y, cellSize, cellSize)
                }
            }
            val text = "Score: $score".toCharArray()
            g.color = Color.GREEN
            g.drawChars(text, 0, text.size, 0, g.fontMetrics.height)
        }

        private fun renderGame() {
            while (output.isNotEmpty()) {
                val tileX = output.removeFirst()
                val tileY = output.removeFirst()
                val tileId = output.removeFirst()
                if (tileX == -1L && tileY == 0L) {
                    score = tileId
                    continue
                }
                when (tileId) {
                    3L -> paddlePosition = Position(tileX, tileY)
                    4L -> ballPosition = Position(tileX, tileY)
                }
                data[Position(tileX, tileY)] = tileId
            }
            repaint()
            Thread.sleep(4)
        }
    }
}