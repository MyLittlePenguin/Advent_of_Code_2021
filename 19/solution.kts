import kotlin.math.abs

//val input = java.io.File("19", "test.txt").readLines()
val input = java.io.File("19", "input.txt").readLines()

data class Vec(var x: Int, var y: Int, var z: Int) {
    operator fun minus(other: Vec) = Vec(this.x - other.x, this.y - other.y, this.z - other.z)
    operator fun plus(other: Vec) = Vec(this.x + other.x, this.y + other.y, this.z + other.z)
    fun copy() = Vec(this.x, this.y, this.z)
    fun revRotate(x: Int, y: Int, z: Int) = this.rotate(4 - x, 4 - y, 4 - z)
    fun rotate(x: Int, y: Int, z: Int): Vec {
        var vec = when (x % 4) {
            0 -> Vec(this.x, this.y, this.z)
            1 -> Vec(this.x, this.z, -this.y)
            2 -> Vec(this.x, -this.y, -this.z)
            else -> Vec(this.x, -this.z, this.y)
        }
        vec = when (y % 4) {
            0 -> vec
            1 -> Vec(vec.z, vec.y, -vec.x)
            2 -> Vec(-vec.x, vec.y, -vec.z)
            else -> Vec(-vec.z, vec.y, vec.x)
        }
        return when (z % 4) {
            0 -> vec
            1 -> Vec(vec.y, -vec.x, vec.z)
            2 -> Vec(-vec.x, -vec.y, vec.z)
            else -> Vec(-vec.y, vec.x, vec.z)
        }
    }

    fun getAllRotations() = (0..3).flatMap { x ->
        (0..3).flatMap { y ->
            (0..3).map { z ->
                this.rotate(x, y, z)
            }
        }
    }

    fun toStr(): String {
        return "%-30s".format(this.toString())
    }
}

data class Relation(val v1: Vec, val v2: Vec, val relations: List<Vec>)

data class Scanner(
    var pos: Vec = Vec(0, 0, 0),
    var beacons: MutableList<Vec> = mutableListOf(),
    var beaconRelations: List<Relation> = mutableListOf()
)

val scanners = mutableListOf<Scanner>()

var currentScanner = Scanner()
input.forEach {
    when {
        it.startsWith("---") -> {
            currentScanner = Scanner()
            scanners.add(currentScanner)
        }
        it.isBlank() -> {}
        else -> {
            it.split(",").map { it.toInt() }.let { pos ->
                currentScanner.beacons.add(Vec(pos[0], pos[1], pos[2]))
            }
        }
    }
}

fun createRelations(scanner: Scanner) {
    val pairs = mutableListOf<Pair<Vec, Vec>>()
    scanner.beacons.forEachIndexed { index, vec ->
        scanner.beacons.forEachIndexed { otherIndex, otherVec ->
            if (otherIndex > index) {
                pairs.add(Pair(vec.copy(), otherVec.copy()))
            }
        }
    }
    scanner.beaconRelations = pairs.map { Relation(it.first, it.second, (it.first - it.second).getAllRotations()) }
}
scanners.forEach {
    createRelations(it)
}

var counter = 1
val scanner0 = scanners.removeFirst()
var foundScanners = mutableListOf<Scanner>()
while(scanners.size > 0) {
    println("Durchgang: " + counter++)
    var foundScanner = Scanner()
    for(scanner in scanners) {
        val foundRelations = mutableListOf<Pair<Relation, Relation>>()
        scanner.beaconRelations.forEach { rel ->
            val relVec = rel.relations.first()
            scanner0.beaconRelations.find { it.relations.contains(relVec) }?.apply { foundRelations.add(Pair(this, rel)) }
        }
        if(foundRelations.size >= 20) {
            println("juhu =) ${foundRelations.size}")

            val (rotNum, relevantRelationPairs) = foundRelations.groupBy { (s, other) -> other.relations.indexOf(s.relations.first()) }
                .entries.sortedByDescending { it.value.size }.first()
            println()

            val xRot = rotNum / 16 % 4
            val yRot = rotNum / 4 % 4
            val zRot = rotNum % 4

            println("$rotNum -> ($xRot, $yRot, $zRot)" )

            val (s, o) = relevantRelationPairs.first()
            var r1 = o.v1.rotate(xRot, yRot, zRot)
            var r2 = o.v2.rotate(xRot, yRot, zRot)
            var diffVec = listOf(r1 - s.v1, r2 - s.v1, r1 - s.v2, r2 - s.v2).groupingBy { it }.eachCount()
                .entries.filter { it.value == 2 }.firstOrNull()?.key

            scanner.pos = diffVec!!
            scanner.beacons = scanner.beacons.map { it.rotate(xRot, yRot, zRot) - diffVec }.toMutableList()
            scanner0.beacons.addAll(scanner.beacons)
            createRelations(scanner)

            scanner0.beaconRelations += scanner.beaconRelations
            scanner0.beacons = scanner0.beacons.distinct().toMutableList()
            foundScanner = scanner
            foundScanners.add(foundScanner)
            break
        }
    }
    scanners.remove(foundScanner)
}
println(scanner0.beacons.size)
var maxDist = 0
foundScanners.forEachIndexed { index, scanner ->
    foundScanners.forEachIndexed { secondIndex, secondScanner ->
        if(secondIndex > index) {
            val (x1, y1, z1) = scanner.pos
            val (x2, y2, z2) = secondScanner.pos
            var dist = abs(x1 - x2) + abs(y1 - y2) + abs(z1 - z2)
            if(dist > maxDist) maxDist = dist
        }
    }
}
println(maxDist)