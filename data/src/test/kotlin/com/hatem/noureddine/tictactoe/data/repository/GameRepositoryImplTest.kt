package com.hatem.noureddine.tictactoe.data.repository

import com.hatem.noureddine.tictactoe.domain.model.Game
import com.hatem.noureddine.tictactoe.domain.model.Player
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameRepositoryImplTest {
    private lateinit var repository: GameRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = GameRepositoryImpl()
    }

    @Test
    fun `game property returns a non-null game instance`() {
        val game = repository.game
        assertNotNull(game)
        assertEquals(3, game.size) // Default size
    }

    @Test
    fun `updateGame updates the game instance`() {
        // Given a new game with different size
        val newGame = Game(size = 4)

        // When updateGame is called
        repository.updateGame(newGame)

        // Then the game property returns the new game
        assertEquals(4, repository.game.size)
    }

    @Test
    fun `state persistence is maintained in memory`() {
        // Given an initial game
        val initialGame = repository.game

        // When we make a move
        initialGame.play(0, 0)

        // And update the repository (simulating saving, though it's in-memory)
        repository.updateGame(initialGame)

        // Then the repository holds the updated state
        val retrievedGame = repository.game
        assertEquals(Player.X, retrievedGame.getCell(0, 0))
    }
}
