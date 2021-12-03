val input = java.io.File("03/input.txt").readLines()
var inputO2 = java.io.File("03/input.txt").readLines()
var inputCo2 = java.io.File("03/input.txt").readLines()

val numSize = input[0].length
var gamma = ""
var epsilon = ""

for(i in 0 until numSize) {
//    println(input[0][i])
    var o2 = '1'
    var co2 = '1'
    var col = ""
    var colO2 = ""
    var colCo2 = ""
    
    input.forEach { col += it[i] }
    inputO2.forEach { colO2 += it[i] }
    inputCo2.forEach { colCo2 += it[i] }
    
    col.groupingBy { it }.eachCount().let {
        gamma += if(it['0']!! > it['1']!!) 0 else 1
        epsilon += if(it['1']!! > it['0']!!) 0 else 1
    }
    colO2.groupingBy { it }.eachCount().let {
        o2 = when {
            it.size == 1 -> it.keys.first()
            it['0']!! > it['1']!! -> '0'
            it['0']!! < it['1']!! -> '1'
            else -> '1'
        }
        
        if(inputO2.size > 1)
        {
            inputO2 = inputO2.filter { it[i] == o2 }
        }
        
    }
    
    colCo2.groupingBy { it }.eachCount().let {
        co2 = when {
            it.size == 1 -> it.keys.first()
            it['0']!! > it['1']!! -> '1'
            it['0']!! < it['1']!! -> '0'
            else -> '0'
        }
        if(inputCo2.size > 1) {
            inputCo2 = inputCo2.filter { it[i] == co2 }
        }
    }
}

println("Energy: " + Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2))
println("Oxygen: " + Integer.parseInt(inputO2[0],2))
println("Co2: " + Integer.parseInt(inputCo2[0],2))
println(Integer.parseInt(inputO2[0], 2) * Integer.parseInt(inputCo2[0], 2))

println(gamma)
println(epsilon)