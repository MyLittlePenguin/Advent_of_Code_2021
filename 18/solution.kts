//val input = java.io.File("18", "test.txt").readLines()
val input = java.io.File("18", "input.txt").readLines()

interface Snail {
    fun magnitude(): Long
    operator fun plus(other: Snail) = SnailPair(this, other)
}

data class SnailNum(var value: Int) : Snail {
    override fun magnitude() = this.value.toLong()
    override fun toString() = this.value.toString()
}

data class SnailPair(var left: Snail, var right: Snail) : Snail {
    override fun magnitude() = this.left.magnitude() * 3 + this.right.magnitude() * 2
    override fun toString() = "[${this.left.toString()}, ${this.right.toString()}]"
}

fun SnailNum.split() =
    (this.value / 2).let { SnailPair(SnailNum(it), SnailNum(if (this.value % 2 == 0) it else it + 1)) }

var explosion = false
var split = false

fun explode(snail: Snail, lvl: Int): Pair<Int, Int> {
    if (snail is SnailNum) return Pair(0, 0)
    snail as SnailPair
    if (snail.left is SnailNum && snail.right is SnailNum) {
        return if (lvl > 4) {
            Pair((snail.left as SnailNum).value, (snail.right as SnailNum).value)
        } else Pair(0, 0)
    } else {
        var residue = explode(snail.left, lvl + 1)
        if (!explosion && (residue.first != 0 || residue.second != 0)) {
            snail.left = SnailNum(0)
            explosion = true
        }
        if (residue.second != 0) {
            var firstNum = snail.right
            while (firstNum is SnailPair) {
                firstNum = firstNum.left
            }
            firstNum as SnailNum
            firstNum.value += residue.second
            return Pair(residue.first, 0)
        }
        if (residue.first != 0) return residue

        if (explosion) return Pair(0, 0)
        residue = explode(snail.right, lvl + 1)
        if (!explosion && (residue.first != 0 || residue.second != 0)) {
            snail.right = SnailNum(0)
            explosion = true
        }
        if (residue.first != 0) {
            var firstNum = snail.left
            while (firstNum is SnailPair) {
                firstNum = firstNum.right
            }
            firstNum as SnailNum
            firstNum.value += residue.first
            return Pair(0, residue.second)
        }
        if (residue.second != 0) return residue
    }
    return Pair(0, 0)
}

fun split(snail: SnailPair): Boolean {
    var result = if (snail.left is SnailPair) {
        split(snail.left as SnailPair)
    } else {
        snail.left as SnailNum
        if ((snail.left as SnailNum).value >= 10) {
            snail.left = (snail.left as SnailNum).split()
            true
        } else false
    }
    return if (result) true
    else {
        if (snail.right is SnailPair) split(snail.right as SnailPair)
        else {
            if ((snail.right as SnailNum).value >= 10) {
                snail.right = (snail.right as SnailNum).split()
                true
            } else false
        }
    }
}

fun reduce(snail: SnailPair): SnailPair {
    do {
        explosion = false
        split = false
        explode(snail, 1)
        if (!explosion) split = split(snail)
    } while (explosion != false || split != false)
    return snail
}

class Parser(val line: String) {
    val iterator = this.line.iterator()
    fun parse(): Snail {
        var c = iterator.next()
        return if (c == '[') {
            val left = parse()
            iterator.next()
            val right = parse()
            iterator.next()
            SnailPair(left, right)
        } else {
            SnailNum(c.digitToInt())
        }
    }
}

val snail = input.map { Parser(it).parse() }.reduce { acc, snail -> reduce(acc + snail) }

println(snail)
println(snail.magnitude())

input.flatMap { current ->
    input.filter { it != current }.map { Parser(current).parse() + Parser(it).parse() }.map { reduce(it).magnitude() }
}.maxOf { it }.let { println(it) }