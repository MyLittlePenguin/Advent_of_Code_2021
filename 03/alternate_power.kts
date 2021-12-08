var length = 0
fun List<String>.parse() = this.map { if(length == 0) length = it.length; it.toInt(2) }

val input = java.io.File("03/input.txt").readLines().parse()
var inputO2 = java.io.File("03/input.txt").readLines().parse()
var inputCo2 = java.io.File("03/input.txt").readLines().parse()

fun Int.filterListe(liste: List<Int>, pos: Int) = liste.filter { it.and(1 shl pos).shr(pos) == this }
fun Int.isCommon(size: Int) = if(size - this > this) 0 else 1
fun Int.notCommon(size: Int) = if(size - this > this) 1 else 0

var gamma = 0
var epsilon = 0
while(--length >= 0) {
    input.count { it and (1 shl length) != 0 }.let {
        gamma = gamma.shl(1) + it.isCommon(input.size)
        epsilon = epsilon.shl(1) + it.notCommon(input.size)
    }
    if(inputO2.size > 1) inputO2 = inputO2.count { it and (1 shl length) != 0 }.isCommon(inputO2.size).filterListe(inputO2, length)
    if(inputCo2.size > 1) inputCo2 = inputCo2.count { it and (1 shl length) != 0 }.notCommon(inputCo2.size).filterListe(inputCo2, length)
}

println("Energy: ${gamma * epsilon}")
println("Air: ${inputO2.first() * inputCo2.first()}")