package isel.leic.tds.checkers.UI

import isel.leic.tds.checkers.model.*

class Command(
    val syntaxArgs: String = "",
    val toTerminate: Boolean = false,
    val execute: (args: List<String>, clash: Clash) -> Clash = { _, clash -> clash }
)

val playCommand = Command("<from> <to>") { args, clash ->
    require(args.size == 2) { "Invalid number of arguments" }
    val from = requireNotNull(args[0].toSquareOrNull()) { "Invalid from position" }
    val to = requireNotNull(args[1].toSquareOrNull()) { "Invalid to position" }
    clash.play(from, to)
}

val startCommand = Command ("<name>"){
    args, clash ->
    val arg = requireNotNull(args.firstOrNull()) { "Missing name" }
    clash.newBoard()
}

fun nameCmd(fx: Clash.(Name) -> Clash) = Command("<name>") { args, clash ->
    val arg = requireNotNull(args.firstOrNull()) { "Missing name" }
    clash.fx(Name(arg))
}

fun getCommands() = mapOf(
    "play" to playCommand,
    "start" to startCommand,
    "exit" to Command(toTerminate = true),
    //"grid" to Command { _, clash -> clash.also { it.show() } },
    "refresh" to Command { _, clash -> clash.refresh() },
    "create" to nameCmd(Clash::start),
    "join" to nameCmd(Clash::join)
)