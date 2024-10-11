package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.UI.*

fun main() {
    var board = initializeBoard()
    val cmd = getCmd()
    while (true) {
        val (name, args) = readLineCommand()
        val command = cmd[name]
        if (command == null) {
            println("Unknown Command $name")
        } else if (!command.syntax(args)) {
            println("Syntax error")
        } else {
            board = command.execute(args, board)
        }
        displayBoard(board)
    }
    }



fun initializeBoard(): Board {
    val grid = mutableMapOf<Square, Char?>()
    return Board(grid, 'W')
}

fun displayBoard(board: Board) {
    board.show()
}