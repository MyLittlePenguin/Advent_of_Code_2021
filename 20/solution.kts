//val input = java.io.File("20", "test.txt").readLines()
val input = java.io.File("20", "input.txt").readLines()

fun readInput(input: List<String>): Pair<List<Int>, List<List<Int>>> {
    val iter = input.iterator()

    val alg = iter.next().map { if(it == '#') 1 else 0 }
    var img = mutableListOf<List<Int>>()
    iter.next()
    while(iter.hasNext()) {
        img.add(iter.next().map { if(it == '#') 1 else 0 })
    }
    return Pair(alg, img)
}

fun enhance(img: List<List<Int>>, alg: List<Int>, infiniteBit: Int): List<List<Int>> {
    var newImg = mutableListOf<MutableList<Int>>()
    for(row in -1 .. img.size) {
        val rowBuffer = mutableListOf<Int>()
        for(col in -1 .. img[0].size) {
            var index = 0
            for(y in row - 1 .. row + 1) {
                for (x in col - 1 .. col + 1) {
                    val nextBit = if(y < 0 || y >= img.size || x < 0 || x >= img[0].size) infiniteBit else img[y][x]
                    index = index.shl(1) + nextBit
                }
            }
            rowBuffer.add(alg[index])
        }
        newImg.add(rowBuffer)
    }
    return newImg
}

fun print(img: List<List<Int>>) {
    img.forEach { row ->
        row.forEach { print(if(it == 1) '#' else '.') }
        println()
    }
}

var (alg, img) = readInput(input)

var infiniteBit = 0
repeat(2) {
    img = enhance(img, alg, infiniteBit)
    infiniteBit = if(infiniteBit == 1) 0 else 1
}
println(img.sumOf { it.sum() })

repeat(48) {
    img = enhance(img, alg, infiniteBit)
    infiniteBit = if(infiniteBit == 1) 0 else 1
}
println(img.sumOf { it.sum() })