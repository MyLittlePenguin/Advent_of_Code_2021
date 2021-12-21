import kotlin.math.min

//val input = java.io.File("21", "test.txt").readLines()
val input = java.io.File("21", "input.txt").readLines()

var start1 = 0
var start2 = 0
var pos1 = 0
var pos2 = 0
var score1 = 0
var score2 = 0
var dieCount = 0
var dieValue = 0
input.map { it[it.length - 1].digitToInt() }.let {
    start1 = it[0]
    start2 = it[1]
}
pos1 = start1
pos2 = start2
var p1 = false
while (score1 < 1000 && score2 < 1000) {
    p1 = !p1

    var sum = dieValue++ % 100 + 1
    sum += dieValue++ % 100 + 1
    sum += dieValue++ % 100 + 1
    dieCount += 3
    if (p1) {
        pos1 = (pos1 + sum - 1) % 10 + 1
        score1 += pos1
    } else {
        pos2 = (pos2 + sum - 1) % 10 + 1
        score2 += pos2
    }
}
println("diceCount: $dieCount player1: $score1 player2: $score2")
println("part1: ${min(score1, score2) * dieCount}")

var possibilities = (1..3).flatMap { x ->
    (1..3).flatMap { y ->
        (1..3).map { z -> x + y + z }
    }
}.groupingBy { it }.eachCount()
println("possibilities: $possibilities")

data class Player(val pos: Int, val score: Int)
data class Game(val p1: Player, val p2: Player, val over: Boolean = false)
var results = mapOf(Game(Player(start1, 0), Player(start2, 0), false) to 1.toLong())

fun calcPlayer(pl: Player, steps: Int): Player {
    var (pos, score) = pl
    pos = (steps + pos - 1) % 10 + 1
    score += pos
    return Player(pos, score)
}

fun Map<Game, Long>.iterate(possibilities: Map<Int, Int>, p1: Boolean): Map<Game, Long> {
    return this.entries.flatMap { universe ->
        val (game, count) = universe
        if(game.over) {
            listOf(game to count)
        }
        else {
            possibilities.map { (steps, probability) ->
                val player1 = if(p1) calcPlayer(game.p1, steps) else game.p1
                val player2 = if(!p1) calcPlayer(game.p2, steps) else game.p2
                Game(player1, player2, player1.score >= 21 || player2.score >= 21) to probability * count
            }
        }
    }.groupBy({ it.first }, { it.second }).entries.map { it.key to it.value.sum() }.toMap()
}

while (results.keys.filter { !it.over }.isNotEmpty()) {
    results = results.iterate(possibilities, true)
    results = results.iterate(possibilities, false)
}
results.entries.groupBy({ it.key.p1.score >= 21 }, { it.value }).forEach { println(it.value.sum()) }