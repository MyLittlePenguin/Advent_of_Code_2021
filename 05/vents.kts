import kotlin.math.max

data class Point(val x: Int, val y: Int) {
    fun invX() = Point(-x, y)
}

data class Line(val begin: Point, val end: Point) {
    fun toPoints(): List<Point> {
        val deltaX = end.x - begin.x
        val deltaY = end.y - begin.y

        return when {
            deltaX < 0 && deltaY > 0 -> Line(begin.invX(), end.invX()).toPoints().map { it.invX() }
            deltaX < 0 -> Line(end, begin).toPoints()
            deltaY < 0 -> Line(end, begin).toPoints()
            deltaX == 0 -> (begin.y..end.y).map { Point(begin.x, it) }
            deltaY == 0 -> (begin.x..end.x).map { Point(it, begin.y) }
            else -> (0..deltaX).map { Point(begin.x + it, begin.y + it) }
        }
    }
}

fun List<Line>.countCrossings() = this.flatMap { it.toPoints() }.groupBy { it }.values.count { it.count() > 1 }

val input = java.io.File("05", "input.txt").readLines()
    .map { it.split(",", " -> ").map { n -> n.toInt() } }
    .map { Line(Point(it[0], it[1]), Point(it[2], it[3])) }

println(input.filter { it.begin.x == it.end.x || it.begin.y == it.end.y }.countCrossings())
println(input.countCrossings())