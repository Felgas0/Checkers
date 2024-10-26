package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.UI.*


fun main() {
    var game: Game = Game() // Estado do tabuleiro
    val cmds: Map<String,Command> = getCommands()
    while (true) {
        val (name, args) = readLineCommand()
        val cmd = cmds[name]
        if (cmd==null) println("Unknown command $name")
        else try {
            game = cmd.execute(args, game)
            if( cmd.toTerminate ) break
            game.show()
        } catch (e: IllegalStateException) {
            println(e.message)
        } catch (e: IllegalArgumentException) {
            println("${e.message}. Use: $name ${cmd.syntaxArgs}")
        }
    }
}






