import java.io.File

typealias Vec = Pair<Int, Int>

fun readInstructions(instruction: String): Vec {
    val op = instruction.split(" ").let { Pair<String, Int>(it.first(), it[1].toInt()) }
    return when(op.first) {
        "forward" -> Vec(op.second, 0)
        "down" -> Vec(0, op.second)
        "up" -> Vec(0, -(op.second))
        else -> Vec(0, 0)
    }
}

operator fun Vec.plus(other: Vec) = Vec(this.first + other.first, this.second + other.second)

fun calcNewPosProduct(input: List<String>) =
    input.map { readInstructions(it) }.reduce { acc: Vec?, unit ->
        if(acc == null) unit else acc + unit
    }.let { it.first * it.second }

fun calcNewPosProduct2(input: List<String>): Int {
    var aim = 0
    var acc = Vec(0, 0)
    
    input.map { readInstructions(it) }.forEach {
        if(it.first == 0)
            aim += it.second
        else acc = Vec(
                acc.first + it.first,
                acc.second + (aim * it.first)
            )
    }
    
    return acc.first * acc.second
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