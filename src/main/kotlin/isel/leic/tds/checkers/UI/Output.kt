package isel.leic.tds.checkers.UI
import isel.leic.tds.checkers.model.*

val BORDER = "  +${"-".repeat(BOARD_DIM * 2 - 1)}+"

fun Board.show() {
    println(BORDER)
    for (row in Row.values) {
        print("${row.digit} |")
        for (col in Column.values) {
            val square = Square(row, col)
            val piece = grid[square]?.toString() ?: if (square.black) '-' else ' '
            print("$piece ")
        }
        println("|")
    }
    println(BORDER)
    print("   ")
    for (col in Column.values) {
        print("${col.symbol} ")
    }
    println()
    println(when(this){
        is BoardRun -> " Turn: ${turn}"
        is BoardWin -> " Winner: ${winner}"
    })
}

fun Game.show() = board?.show()

fun Game.showScore(){
    Player.entries.forEach{
        println("${it} : ${score[it]}")
    }
}