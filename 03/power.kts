val input = java.io.File("03/input.txt").readLines()
var inputO2 = java.io.File("03/input.txt").readLines()
var inputCo2 = java.io.File("03/input.txt").readLines()

fun List<String>.countAtPos(i: Int) = this.groupingBy { it[i] }.eachCount()
fun Char.filterListe(liste: List<String>, pos: Int): List<String> = liste.filter { it[pos] == this }
fun String.b2i() = Integer.parseInt(this, 2)

fun Map<Char, Int>.evalZeros(commonZero: Char, commonOne: Char) = when {
    this.size == 1 -> this.keys.first()
    this['0']!! > this['1']!! -> commonZero
    else -> commonOne
}

var gamma = ""
var epsilon = ""

for(i in 0 until input[0].length) {
    input.countAtPos(i).let {
        gamma += it.evalZeros('0', '1')
        epsilon += it.evalZeros('1', '0')
    }
    inputO2 = inputO2.countAtPos(i).evalZeros('0', '1').filterListe(inputO2, i)
    inputCo2 = inputCo2.countAtPos(i).evalZeros('1', '0').filterListe(inputCo2, i)
}

println("Energy: " + gamma.b2i() * epsilon.b2i())
println("Air: " + (inputO2[0].b2i() * inputCo2[0].b2i()))