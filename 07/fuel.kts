import kotlin.math.absoluteValue

val input = java.io.File("07/input.txt").readText().split(",").map { it.toInt() }

tailrec fun sum(n: Int, m: Long = 0): Long = if(n > 1) sum(n - 1, m + n) else m + n

val possibilities = input.sorted().let { it.first() .. it.last() }
possibilities.map { input.map { crab -> (crab - it).absoluteValue }.sum() }.sorted().first().let { println(it) }
possibilities.map { input.map { crab -> sum((crab - it).absoluteValue) }.sum() }.sorted().first().let { println(it) }