import java.io.File

data class Vec(var first: Int, var second: Int) {
    operator fun plus(other: Vec) = Vec(this.first + other.first, this.second + other.second)
}

fun readInstructions(instruction: String): Vec {
    val op = instruction.split(" ").let { Pair<String, Int>(it.first(), it[1].toInt()) }
    return when(op.first) {
        "forward" -> Vec(op.second, 0)
        "down" -> Vec(0, op.second)
        "up" -> Vec(0, -(op.second))
        else -> Vec(0, 0)
    }
}

inline fun calcNewPosProduct(input: List<String>, mapper: (Vec) -> Vec = { it }) =
    input.map { readInstructions(it) }.map(mapper).reduce { acc, unit -> acc + unit }.let { it.first * it.second }

fun calcNewPosProduct2(input: List<String>): Int {
    var aim = 0
    return calcNewPosProduct(input) {
        aim += it.second
        Vec(it.first, aim * it.first)
    }
}

val input = File("02/input.txt").readLines(Charsets.UTF_8)

println(
    calcNewPosProduct(input)
)

println(
    calcNewPosProduct2(listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2"))
)

println(
    calcNewPosProduct2(input)
)