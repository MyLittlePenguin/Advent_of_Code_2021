import kotlin.math.absoluteValue

val input = java.io.File("07/input.txt").readText().split(",").map { it.toInt() }

fun fac(n: Int): Long = (if(n > 1) fac(n - 1) else 0 ) + n

val possibilities = input.sorted().let { it.first() .. it.last() }
possibilities.map { Pair(it, input.map { crab -> (crab - it).absoluteValue }.sum()) }.sortedBy { it.second }.first().let { println(it) }
possibilities.map { Pair(it, input.map { crab -> fac((crab - it).absoluteValue) }.sum()) }.sortedBy { it.second }.first().let { println(it) }