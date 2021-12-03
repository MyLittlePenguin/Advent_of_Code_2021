data class Vec(var distance: Int, var depth: Int, var aim: Int = 0) {
    operator fun plus(other: Vec) = Vec(this.distance + other.distance, this.depth + other.depth)
}

fun List<String>.readInstructions(): List<Vec> = this.map {
        val op = it.split(" ").let { Pair<String, Int>(it[0], it[1].toInt()) }
        when (op.first) {
            "forward" -> Vec(op.second, 0)
            "down" -> Vec(0, op.second)
            "up" -> Vec(0, -(op.second))
            else -> Vec(0, 0)
        }
    }

fun List<Vec>.calcNewPosProduct(): Int = this.reduce { acc, unit -> acc + unit }.let { it.distance * it.depth }

fun List<Vec>.calcNewPosProduct2(): Int = this.windowed(2, 1, true) {
        if(it.size == 2) it[1].aim = it[1].depth + if(it[0].aim == 0) it[0].depth else it[0].aim
        it[0].apply { depth = it[0].distance * it[0].aim }
    }.calcNewPosProduct()

val input = java.io.File("02/test_input.txt").readLines(Charsets.UTF_8).readInstructions()

println( input.calcNewPosProduct() )
println( input.calcNewPosProduct2() )