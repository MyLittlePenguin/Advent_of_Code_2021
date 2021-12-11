import java.io.File

if(args.size != 1) {
    println("ERROR: invalid number of Arguments!")
    println("usage: kotlin createDay.kts <day>")
    System.exit(1)
}

val day = args[0]

File("template").copyRecursively(File(day), overwrite = false)

File(day, "solution.kts").let {
    it.writeText(it.readText().replace("{day}", day))
}