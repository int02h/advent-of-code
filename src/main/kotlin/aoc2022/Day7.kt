package aoc2022

import Input

object Day7 {

    fun part1(input: Input) {
        val fileSystem = initFileSystem(input)
        val directories = mutableListOf<File>()
        fileSystem.root.getAllDirectories(directories)
        directories.filter { it.totalSize <= 100000 }
            .sumOf { it.totalSize }
            .also(::println)
    }

    fun part2(input: Input) {
        val fileSystem = initFileSystem(input)
        val directories = mutableListOf<File>()
        fileSystem.root.getAllDirectories(directories)

        val diskSpace = 70000000
        val updateSpace = 30000000
        val usedSpace = fileSystem.root.totalSize
        val freeSpace = diskSpace - updateSpace
        val needToFreeSpace = usedSpace - freeSpace

        val toDelete = directories.filter { it.totalSize >= needToFreeSpace }.minBy { it.totalSize }
        println(toDelete.totalSize)
    }

    private fun initFileSystem(input: Input): FileSystem {
        val fileSystem = FileSystem()
        input.asText()
            .split("$ ")
            .filter { it.isNotEmpty() }
            .map { it.trim().split('\n') }
            .forEach { fileSystem.handleCommand(it.first(), it.drop(1)) }
        return fileSystem
    }

    private class FileSystem {
        val root = File(name = "/", parent = null, isDirectory = true)
        private var currentDir = root

        fun handleCommand(commandWithArg: String, output: List<String>) {
            val (cmd, arg) = commandWithArg.split(' ')
                .let { it[0] to it.getOrNull(1).orEmpty() }
            when (cmd) {
                "cd" -> {
                    currentDir = when (arg) {
                        "/" -> root
                        ".." -> requireNotNull(currentDir.parent)
                        else -> currentDir.resolve(arg)
                    }
                }
                "ls" -> {
                    output.map { it.split(' ') }
                        .forEach { (typeOrSize, name) ->
                            if (typeOrSize == "dir") {
                                currentDir.createDir(name)
                            } else {
                                currentDir.createFile(name, typeOrSize.toInt())
                            }
                        }
                }
                else -> error("Unknown command $cmd")
            }
        }
    }

    private class File(val name: String, val parent: File?, val isDirectory: Boolean, val size: Int = 0) {

        private val files = mutableMapOf<String, File>()

        val totalSize: Int by lazy {
            if (isDirectory) {
                files.values.sumOf { it.totalSize }
            } else {
                size
            }
        }

        fun resolve(name: String): File = files.getValue(name)

        fun createDir(name: String) {
            files[name] = File(name = name, parent = this, isDirectory = true)
        }

        fun createFile(name: String, size: Int) {
            files[name] = File(name = name, parent = this, isDirectory = false, size = size)
        }

        fun getAllDirectories(result: MutableList<File>) {
            files.values.filter { it.isDirectory }.forEach { dir ->
                result += dir
                dir.getAllDirectories(result)
            }
        }

    }
}