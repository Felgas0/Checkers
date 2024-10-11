package isel.leic.tds.checkers.UI
import isel.leic.tds.checkers.model.*

abstract class Command {
    open fun syntax(args:List<String>):Boolean = args.isEmpty()
    abstract fun execute(board: Board?, args: List<String>): Board
    open fun kill():Boolean = false
}

class StartCmd: Command() {
    override fun syntax(args: List<String>): Boolean = args.size == 1
    override fun execute(board: Board?, args: List<String>): Board {
        TODO()
    }

}

class PlayCmd: Command(){
    override fun syntax(args: List<String>): Boolean = args.size == 2
    override fun execute(board: Board?, args: List<String>): Board {
        TODO("Not yet implemented")
    }

}

class gridCmd:Command(){
    override fun syntax(args: List<String>): Boolean = args.isEmpty()
    override fun execute(board: Board?, args: List<String>): Board {
        TODO("Not yet implemented")
    }
}

class RefreshCmd:Command(){
    override fun syntax(args: List<String>): Boolean = args.isEmpty()
    override fun execute(board: Board?, args: List<String>): Board {
        TODO("Not yet implemented")
    }
}

class ExitCmd: Command(){
    override fun syntax(args: List<String>): Boolean = args.isEmpty()
    override fun execute(board: Board?, args: List<String>): Board {
        TODO("Not yet implemented")
    }
}