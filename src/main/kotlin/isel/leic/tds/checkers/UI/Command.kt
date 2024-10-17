package isel.leic.tds.checkers.UI
import isel.leic.tds.checkers.model.*

abstract class Command (val syntaxArgs: String = ""){
    abstract fun execute(board: Board?, args: List<String>): Board
    open fun exit():Boolean = false
}

class StartCmd: Command() {
    override fun execute(board: Board?, args: List<String>): Board {
        if (args.size > 1) {
            throw IllegalArgumentException("Invalid number of arguments")
        }
        val newBoard = Board(mutableMapOf(), Player.w)
        return newBoard
    }

}

class PlayCmd: Command(){
    override fun execute(board: Board?, args: List<String>): Board {
        if (args.size != 2) {
            throw IllegalArgumentException("Invalid number of arguments")
        }
        val firstArg = requireNotNull(args[0].toSquareOrNull()) { "Quadrado Inválido" }
        val secondArg = requireNotNull(args[1].toSquareOrNull()) { "Quadrado Inválido" }
        check(board != null) { "No Board" }
        return board.tryPlay(firstArg, secondArg)
    }

}

class gridCmd:Command(){
    override fun execute(board: Board?, args: List<String>): Board{
        if (args.size != 0) {
            throw IllegalArgumentException("Invalid number of arguments")
        }
        check(board != null) { "No Board" }
        board.show()
        return board
    }
}

class RefreshCmd:Command(){
    override fun execute(board: Board?, args: List<String>): Board {
        if (args.size != 0) {
            throw IllegalArgumentException("Invalid number of arguments")
        }
        TODO("Not yet implemented")
    }
}

class ExitCmd: Command(){
    override fun execute(board: Board?, args: List<String>): Board {
        if (args.size != 0) {
            throw IllegalArgumentException("Invalid number of arguments")
        }
        return board!!
    }
    override fun exit(): Boolean = true
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