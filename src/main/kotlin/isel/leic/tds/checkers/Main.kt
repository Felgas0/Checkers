package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.UI.*

fun main() {
    var board = Board(mutableMapOf(), 'w')
    val cmd = getCmd()
    while (true) {
        val (name, args) = readLineCommand()
        val command = cmd[name]
        if (command == null) {
            println("Unknown Command $name")
        } else if (!command.syntax(args)) {
            println("Syntax error")
        } else {
            board = command.execute(board, args)
            displayBoard(board)
            if (command.exit()) break
        }
    }
    }





fun displayBoard(board: Board) {
    board.show()
}

fun getCmd(): Map<String, Command> {
    return mapOf(
        "start" to StartCmd(),
        "play" to PlayCmd(),
        "grid" to gridCmd(),
        "refresh" to RefreshCmd(),
        "exit" to ExitCmd()
    )
}