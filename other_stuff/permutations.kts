inline fun <reified T> Array<T>.switch(a: Int, b: Int) = Array(this.size) {
    when(it) {
        a -> this[b]
        b -> this[a]
        else -> this[it]
    }
}

//abcdefg
//abcdegf
//abcdgfe
//abcdgef
//abcgefd
//

inline fun <reified T> permutate(arr: Array<T>): List<Array<T>> {
    val mutations = mutableListOf<Array<T>>()
    val lastPos = arr.size - 1
    mutations.add(arr)
    for(i in 2 .. arr.size) {
        val pivot = arr.size - i
        val base = arr.switch(pivot, lastPos)
        for(j in pivot  until arr.size) {
            val tmp = base.switch(j, lastPos)
            mutations.add(tmp)
            println("$pivot $j ${tmp.joinToString("")}")
        }
    }
    return mutations
}

val result = permutate("abcdefg".toCharArray().toTypedArray())
//result.forEach { println(it.joinToString("")) }
println(result.size)
