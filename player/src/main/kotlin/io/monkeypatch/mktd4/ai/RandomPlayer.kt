package io.monkeypatch.mktd4.ai

import io.monkeypatch.mktd4.extension.nextRandomOrElse
import io.monkeypatch.mktd4.model.Game
import io.monkeypatch.mktd4.model.Move
import org.springframework.stereotype.Component

@Component
class RandomPlayer : IPlayer {

    var lastMove: Move? = null

    override fun play(game: Game): Move {
        val attempts = Move.values().toMutableList()
        attempts.remove(Move.NONE)
        attempts.remove(lastMove?.opposite())
        var move = attempts.nextRandomOrElse { Move.NONE }
        val currentTeam = game.currentTeam()
        while (!game.canMove(currentTeam, move) && !attempts.isEmpty() && move != Move.NONE) {
            attempts.remove(move)
            move = attempts.nextRandomOrElse { Move.NONE }
        }
        lastMove = if (!attempts.isEmpty()) move else
            lastMove?.opposite() ?: Move.NONE

        return lastMove!!
    }
}