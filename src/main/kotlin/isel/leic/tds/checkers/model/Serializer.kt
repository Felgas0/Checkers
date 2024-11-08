package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.storage.Serializer

object GameSerializer: Serializer<Game> {
    override fun serialize(data: Game) = buildString {
        appendLine(data.score.entries.joinToString(" ") { (player, points) -> if(player == null)"draw:$points" else "$player:$points" })
        appendLine(data.firstPlayer)
        data.board?.let { append(BoardSerializer.serialize(it)) }
    }

    override fun deserialize(text: String): Game {
        val parts = text.split("\n")
        return Game(
            board = if (parts.size == 2) null else BoardSerializer.deserialize(parts[2]),
            firstPlayer = Player.valueOf(parts[1]),
            score = parts[0].split(" ")
                .map { it.split(":") }
                .associate { (player, points) ->
                    Player.entries.firstOrNull { it.name == player } to points.toInt()
                }
        )
    }
}

object BoardSerializer: Serializer<Board> {
    override fun serialize(data: Board): String =
        when (data) {
            is BoardRun -> "RUN ${data.turn}"
            is BoardWin -> "WIN ${data.winner}"
        } + " | " +
                data.grid.entries.joinToString(" ") { (square, piece) -> "$square:${piece ?: "empty"}" }

    override fun deserialize(text: String): Board {
        val (left, right) = text.split(" | ")
        val moves = if (right.isEmpty()) mapOf() else right
            .split(" ").map { it.split(":") }
            .associate { (square, piece) ->
                square.toSquare() to piece.toPieceOrNull()
            }
        val (type, player) = left.split(" ")
        return when (type) {
            "RUN" -> BoardRun(moves, Player.valueOf(player))
            "WIN" -> BoardWin(moves, Player.valueOf(player))
            else -> error("Illegal board type $type")
        }
    }
}

private fun String.toPieceOrNull(): Piece = when (this) {
    "w" -> Pawn(Player.w)
    "b" -> Pawn(Player.b)
    "W" -> Queen(Player.w)
    "B" -> Queen(Player.b)
    else -> error("Illegal piece $this")
}

