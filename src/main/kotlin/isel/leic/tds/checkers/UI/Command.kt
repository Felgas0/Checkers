package isel.leic.tds.checkers.UI

import isel.leic.tds.checkers.model.*

abstract class Command(val syntaxArgs: String = "") {
    open fun execute(args: List<String>, game: Game): Game = game
    open val toTerminate: Boolean = false
}

object PlayCommand : Command("<from> <to>") {
    override fun execute(args: List<String>, game: Game): Game {
        require(args.size == 2) { "Invalid number of arguments" }
        val from = requireNotNull(args[0].toSquareOrNull()) { "Invalid from position" }
        val to = requireNotNull(args[1].toSquareOrNull()) { "Invalid to position" }
        return game.play(from, to)
    }
}

object StartCommand : Command("<name>") {
    override fun execute(args: List<String>, game: Game): Game {
        require(args.size == 1) { "Game must have a name" }
        return game.new()
    }
}

fun getCommands() = mapOf(
    "play" to PlayCommand,
    "start" to StartCommand,
    "exit" to object : Command() { override val toTerminate = true },
    "grid" to object : Command() {
        override fun execute(args: List<String>, game: Game): Game =
            game.show().let { game }
    },
    "refresh" to object : Command() {
        override fun execute(args: List<String>, game: Game): Game =
            game.also { it.show() }
    }
)