package isel.leic.tds.checkers.UI

data class LineCommand(val name :String, val args: List<String>)

tailrec fun readLineCommand(): LineCommand {
    println()
    print("> ")
    val line = readln().trim().lowercase().split(' ')
    return if (line.isNotEmpty())
        LineCommand(line[0], line.drop(1))
    else readLineCommand()
}