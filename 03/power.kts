val input = java.io.File("03/input.txt").readLines()
var inputO2 = java.io.File("03/input.txt").readLines()
var inputCo2 = java.io.File("03/input.txt").readLines()

fun Char.filterListe(liste: List<String>, pos: Int): List<String> = liste.filter { it[pos] == this }
fun String.b2i() = Integer.parseInt(this, 2)

fun Int.isCommon(size: Int) = if(size - this > this) '0' else '1'
fun Int.notCommon(size: Int) = if(size - this > this) '1' else '0'

var gamma = ""
var epsilon = ""

for(i in 0 until input[0].length) {
    input.count { it[i] == '1' }.let {
        gamma += it.isCommon(input.size)
        epsilon += it.notCommon(input.size)
    }
    if(inputO2.size > 1) inputO2 = inputO2.count { it[i] == '1' }.isCommon(inputO2.size).filterListe(inputO2, i)
    if(inputCo2.size > 1) inputCo2 = inputCo2.count { it[i] == '1' }.notCommon(inputCo2.size).filterListe(inputCo2, i)
}

println("Energy: " + gamma.b2i() * epsilon.b2i())
println("Air: " + (inputO2[0].b2i() * inputCo2[0].b2i()))