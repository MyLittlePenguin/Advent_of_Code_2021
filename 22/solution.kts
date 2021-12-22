//val input = java.io.File("22", "test.txt").readLines()
val input = java.io.File("22", "input.txt").readLines()

data class Cube(val x: Long, val y: Long, val z: Long) {
    val size: Long get() = x * y * z
    operator fun minus(other: Cube) = Cube(this.x - other.x, this.y - other.y, this.z - other.z)
    operator fun plus(other: Cube) = Cube(this.x + other.x, this.y + other.y, this.z + other.z)
    infix fun lessOrEqual(other: Cube) = this.x <= other.x && this.y <= other.y && this.z <= other.z
}

data class Cuboid(val from: Cube, val until: Cube) {
    val size: Cube get() = this.until - this.from + Cube(1, 1, 1)
    operator fun contains(other: Cuboid) = this.from lessOrEqual other.from && other.until lessOrEqual this.until
    operator fun contains(cube: Cube) = this.from lessOrEqual cube && cube lessOrEqual this.until
    fun corners(additiv: Long = 0L) = listOf(
        this.from,
        Cube(this.from.x, this.from.y, this.until.z + additiv),
        Cube(this.from.x, this.until.y + additiv, this.from.z),
        Cube(this.from.x, this.until.y + additiv, this.until.z + additiv),
        Cube(this.until.x + additiv, this.from.y, this.from.z),
        Cube(this.until.x + additiv, this.from.y, this.until.z + additiv),
        Cube(this.until.x + additiv, this.until.y + additiv, this.from.z),
        this.until + Cube(additiv, additiv, additiv)
    )

    fun split(cube: Cube): List<Cuboid> {
        return (
            if (this.from.x < cube.x && this.until.x >= cube.x) {
                listOf(
                    Cuboid(this.from, Cube(cube.x - 1, this.until.y, this.until.z)),
                    Cuboid(Cube(cube.x, this.from.y, this.from.z), this.until)
                )
            } else listOf(this)
            ).flatMap {
                if (it.from.y < cube.y && it.until.y >= cube.y) {
                    listOf(
                        Cuboid(it.from, Cube(it.until.x, cube.y - 1, it.until.z)),
                        Cuboid(Cube(it.from.x, cube.y, it.from.z), it.until)
                    )
                } else listOf(it)
            }.flatMap {
                if (it.from.z < cube.z && it.until.z >= cube.z) {
                    listOf(
                        Cuboid(it.from, Cube(it.until.x, it.until.y, cube.z - 1)),
                        Cuboid(Cube(it.from.x, it.from.y, cube.z), it.until)
                    )
                } else listOf(it)
            }
    }

    fun split(other: Cuboid): List<Cuboid> {
        var cuboids = listOf(this)
        other.corners(1).forEach { corner ->
            cuboids = cuboids.flatMap { it.split(corner) }
        }
        return cuboids
    }
}

fun parseInput(input: List<String>): List<Cuboid> {
    var cubes = listOf<Cuboid>()
    input.forEach {
        val (isOn, coordinateString) = if (it.startsWith("on")) Pair(true, it.substring(3))
        else Pair(false, it.substring(4))

        val coordinateRanges = coordinateString.split(",").map { it.substring(2).split("..").map { it.toLong() } }
        val cuboid = Cuboid(
            Cube(coordinateRanges[0][0], coordinateRanges[1][0], coordinateRanges[2][0]),
            Cube(coordinateRanges[0][1], coordinateRanges[1][1], coordinateRanges[2][1])
        )
        cubes = cubes.flatMap { cube ->
            cube.split(cuboid).let { chunks ->
                if (chunks.filter { it in cuboid }.size > 0) chunks
                else listOf(cube)
            }
        }.filter { it !in cuboid }

        if (isOn) {
            cubes = cubes + cuboid
        }
    }
    return cubes
}

val relevant = Cuboid(Cube(-50, -50, -50), Cube(50, 50, 50))
val cubes = parseInput(input)
val part1 = cubes.filter {
    it.from in relevant || it.until in relevant
}

println(part1.sumOf { it.size.size })
println(cubes.sumOf { it.size.size })