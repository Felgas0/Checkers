package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.UI.*

fun main() {
    var board: Board? = null // Estado do tabuleiro
    val cmds: Map<String,Command> = getCmd()
    while (true) {
        val (name, args) = readLineCommand()
        val cmd = cmds[name]
        if (cmd==null) println("Unknown command $name")
        else try {
            board = cmd.execute(board, args)
            if( cmd.exit() ) break
            board?.show()
        } catch (e: IllegalStateException) {
            println(e.message)
        } catch (e: IllegalArgumentException) {
            println("${e.message}. Use: $name ${cmd.syntaxArgs}")
        }
    }
}




fun displayBoard(board: Board) {
    board.show()
}

