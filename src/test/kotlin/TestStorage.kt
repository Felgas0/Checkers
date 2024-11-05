import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.storage.Storage
import isel.leic.tds.checkers.storage.TextFileStorage
import org.junit.jupiter.api.*
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestStorage {

    private lateinit var storage: Storage<Name, Game>
    private lateinit var tempFile: File

    @BeforeAll
    fun setup() {
        tempFile = File.createTempFile("testStorage", ".tmp")
        storage = TextFileStorage(tempFile.parent, GameSerializer)
    }

    @AfterAll
    fun teardown() {
        tempFile.delete()
    }

    @Test
    fun `test create and read`() {
        val name = Name("testStorage")
        val board = BoardRun(mapOf("1a".toSquare() to Pawn(Player.w)), Player.w)
        val game = Game(board, firstPlayer = Player.w)
        storage.create(name, game)
        val retrievedGame = storage.read(name)
        assertNotNull(retrievedGame)
        assertEquals(game, retrievedGame)
    }

    @Test
    fun `test update`() {
        val name = Name("testGame")
        val board = BoardRun(mapOf("1a".toSquare() to Pawn(Player.w)), Player.w)
        val game = Game(board, firstPlayer = Player.w)
        storage.create(name, game)
        val updatedGame = game.play("1a".toSquare(), "2b".toSquare()) // Assuming play method exists
        storage.update(name, updatedGame)
        val retrievedGame = storage.read(name)
        assertNotNull(retrievedGame)
        assertEquals(updatedGame, retrievedGame)
    }

    @Test
    fun `test delete`() {
        val name = Name("testGame")
        val game = Game(firstPlayer = Player.w)
        storage.create(name, game)
        check(storage.read(name) != null) { "Game not created" }
        storage.delete(name)
        val retrievedGame = storage.read(name)
        assertNull(retrievedGame)
    }
}