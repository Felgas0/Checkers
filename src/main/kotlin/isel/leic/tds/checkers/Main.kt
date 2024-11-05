package isel.leic.tds.checkers

import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.UI.*
import isel.leic.tds.checkers.storage.*


fun main() {
    val storage = TextFileStorage<Name,Game>("games",GameSerializer)
    var clash = Clash(storage)
    val cmds: Map<String,Command> = getCommands()
    while (true) {
        val (name, args) = readLineCommand()
        val cmd = cmds[name]
        if (cmd==null) println("Unknown command $name")
        else try {
            clash = cmd.execute(args,clash)
            if( cmd.toTerminate ) break
            clash.show()
        } catch (e: IllegalStateException) {
            println(e.message)
        } catch (e: IllegalArgumentException) {
            println("${e.message}. Use: $name ${cmd.syntaxArgs}")
        }
    }
}






