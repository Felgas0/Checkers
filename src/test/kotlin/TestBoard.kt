import isel.leic.tds.checkers.model.*
import kotlin.test.*
class TestBoard {
    @Test
    fun `test initialization`(){
        val board = Board(BOARD)
        assertNotNull(board.getPiece(Position(0,1)))
    }
}