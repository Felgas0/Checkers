package isel.leic.tds.checkers.model

/*
data class PlayerPiece(val player: Player, val piece: Piece) {
    enum class Player() {
        w, b;
        val other get() = if (this == w) b else w
    }
    enum class Piece() {
        normal, checker;
        val isChecker get() = this == checker
    }
}

 */

enum class Player {
    w,b;
    val other get() = if (this == w) b else w
}