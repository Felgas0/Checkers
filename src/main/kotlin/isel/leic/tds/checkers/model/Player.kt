package isel.leic.tds.checkers.model

enum class Player() {
    w, b;
    val other get() = if (this == w) b else w
}
