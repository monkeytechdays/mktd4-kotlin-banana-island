package io.monkeypatch.mktd4.ai

import io.monkeypatch.mktd4.model.Game
import io.monkeypatch.mktd4.model.Move


interface IPlayer {

    fun play(game : Game): Move
}