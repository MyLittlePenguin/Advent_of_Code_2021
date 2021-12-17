import kotlin.math.max
import kotlin.math.min

//val input = java.io.File("17", "test.txt").readText().substring("target area: x=".length).split(", y=").map { it.split("..").map { it.toLong() } }
val input = java.io.File("17", "input.txt").readText().substring("target area: x=".length).split(", y=").map { it.split("..").map { it.toLong() } }

data class Vec(var x: Long, var y: Long) {
    fun between(a: Vec, b: Vec) = (min(a.x, b.x) <= this.x && max(a.x, b.x) >= this.x)
        && (min(a.y, b.y) <= this.y && max(a.y, b.y) >= this.y)
}

fun gauss(i: Long) = i * (i + 1) / 2
fun calcEndX(start: Long, cycles: Long) = if (start > cycles) (start - cycles) * cycles + gauss(cycles) else gauss(start)
fun calcStartX(end: Long, cycles: Long) = ((end - gauss(cycles)) / cycles) + cycles
fun calcEndY(start: Long, cycles: Long) = start * cycles - gauss(cycles - 1)
fun calcStartY(end: Long, cycles: Long) = (end + gauss(cycles - 1)) / cycles

val targetBegin = Vec(min(input[0][0], input[0][1]), max(input[1][0], input[1][1]))
val targetEnd = Vec(max(input[0][0], input[0][1]), min(input[1][0], input[1][1]))
val depthBegin = Vec(0, targetBegin.y)
val depthEnd = Vec(0, targetEnd.y)

var results = (1..1000L).flatMap { i ->
    (targetEnd.y..targetBegin.y).map { y -> Pair(calcStartY(y, i), i) }
}.filter { Vec(0, calcEndY(it.first, it.second)).between(depthBegin, depthEnd) }
    .distinct().sortedByDescending { it.first }.flatMap { yc ->
        (1..targetEnd.x).map { x -> Pair(Vec(x, yc.first), yc.second) }.filter {
            Vec(calcEndX(it.first.x, it.second), calcEndY(it.first.y, it.second)).between(targetBegin, targetEnd)
        }
    }.map { it.first }

results.first().let { println("$it -> ${gauss(it.y)}") }
println(results.distinct().count())