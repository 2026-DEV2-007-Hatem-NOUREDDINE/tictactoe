package com.hatem.noureddine.tictactoe.domain.repository

import com.hatem.noureddine.tictactoe.domain.model.Game

class FakeGameRepository : GameRepository {
    override var game: Game = Game()

    override fun updateGame(newGame: Game) {
        game = newGame
    }
}
