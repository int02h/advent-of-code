package util

class Counter<K> {
    private val data = mutableMapOf<K, Int>()

    fun count(key: K) {
        data[key] = data.getOrDefault(key, 0) + 1
    }

    fun getGreatestCountKey(): K = data.maxBy { it.value }.key

    fun getLeastCountKey(): K = data.minBy { it.value }.key
}