package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.UI.show
import isel.leic.tds.checkers.storage.Storage

@JvmInline
value class Name(private val value: String) {
    init { require(isValid(value)) { "Invalid name" } }
    override fun toString() = value
    companion object {
        fun isValid(value: String) =
            value.isNotEmpty() && value.all { it.isLetterOrDigit() } && value.none { it == ' ' }
    }
}

typealias GameStorage = Storage<Name, Game>

open class Clash(val st: GameStorage)

fun Clash.show() {
    when (this) {
        is ClashRun -> {
            println("Game ID: $id")
            game.show()
        }
        else -> println("No active game.")
    }
}

class ClashRun(
    st: GameStorage,
    val game: Game,
    val sidePlayer: Player,
    val id: Name,
) : Clash(st)

fun Clash.start(id: Name): Clash {
    val game = Game(firstPlayer = Player.w)
    st.create(id, game)
    return ClashRun(st, game, Player.w, id)
}

fun Clash.join(id: Name): Clash {
    val game = requireNotNull(st.read(id)) { "Clash not started" }
    return ClashRun(st, game, Player.b, id)
}

private fun Clash.runOper(oper: ClashRun.() -> Game): Clash {
    check(this is ClashRun) { "Clash not started" }
    return ClashRun(st, oper(), if(sidePlayer == Player.w) Player.b else Player.w , id)
}

fun Clash.refresh() = runOper {
    (st.read(id) as Game).also { check(game != it) { "No modification" } }
}

fun Clash.newBoard() = runOper {
    game.new().also { st.update(id, it) }
}

fun Clash.play(from: Square, to: Square) = runOper {
    game.play(from, to).also {
        check(sidePlayer != (game.board as BoardRun).turn) { "Not your turn" }
        st.update(id, it)
    }
}