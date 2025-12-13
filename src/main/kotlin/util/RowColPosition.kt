package util

data class RowColPosition(val row: Int, val col: Int) {
    val up: RowColPosition by lazy { copy(row = row - 1) }
    val upRight: RowColPosition by lazy { copy(row = row - 1, col = col + 1) }
    val upLeft: RowColPosition by lazy { copy(row = row - 1, col = col - 1) }
    val down: RowColPosition by lazy { copy(row = row + 1) }
    val downRight: RowColPosition by lazy { copy(row = row + 1, col = col + 1) }
    val downLeft: RowColPosition by lazy { copy(row = row + 1, col = col - 1) }
    val left: RowColPosition by lazy { copy(col = col - 1) }
    val right: RowColPosition by lazy { copy(col = col + 1) }
}
