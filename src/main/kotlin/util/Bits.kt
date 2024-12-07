package util

fun Int.getBit(index: Int): Boolean = (this and 1.shl(index)) > 0

fun Int.getBitInt(index: Int): Int = if (getBit(index)) 1 else 0