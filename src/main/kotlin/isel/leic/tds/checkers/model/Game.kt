package isel.leic.tds.checkers.model

typealias Score = Map<Player?, Int>

data class Game(
    val board: Board? = null,
    val score: Score = (Player.entries + null).associateWith { 0 },
    val firstPlayer: Player = Player.entries.first()
)

private fun Game.advanceScore(winner: Player?):Score = score - winner + (winner to checkNotNull(score[winner]) + 1)

fun Game.new(): Game = Game(
        board = createInitialBoard(firstPlayer),
        score = if(board is BoardRun) advanceScore(board.turn.other) else score,
        firstPlayer = firstPlayer.other
    )

fun Game.play(from: Square, to: Square): Game {
    checkNotNull(board) { "No board" }
    val newBoard = board.play(from, to)
    return copy(
        board = newBoard,
        score = when(newBoard) {
            is BoardWin -> advanceScore(newBoard.winner)
            else -> score
        }

    )
}