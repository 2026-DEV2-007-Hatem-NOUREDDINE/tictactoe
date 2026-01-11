package com.hatem.noureddine.tictactoe.domain.usecase

import com.hatem.noureddine.tictactoe.domain.model.GameException
import com.hatem.noureddine.tictactoe.domain.model.Player
import com.hatem.noureddine.tictactoe.domain.repository.FakeGameRepository
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class PlayTurnUseCaseTest {
    @Test
    fun `invoke calls play on the game and updates repository`() {
        // Given
        val repository = FakeGameRepository()
        val playTurnUseCase = PlayTurnUseCase(repository)

        // When
        playTurnUseCase(0, 0)

        // Then
        assertSame(Player.O, repository.game.currentPlayer)
    }

    @Test
    fun `invoke throws GameException when move is invalid`() {
        // Given
        val repository = FakeGameRepository()
        val playTurnUseCase = PlayTurnUseCase(repository)
        playTurnUseCase(0, 0) // X plays

        // When & Then
        assertThrows(GameException::class.java) {
            playTurnUseCase(0, 0) // Invalid move
        }
    }
}
