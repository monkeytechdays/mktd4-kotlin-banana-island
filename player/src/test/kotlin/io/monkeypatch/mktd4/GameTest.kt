package io.monkeypatch.mktd4

import io.monkeypatch.mktd4.extension.asString
import io.monkeypatch.mktd4.model.Game
import io.monkeypatch.mktd4.model.MapTile
import io.monkeypatch.mktd4.model.Position
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertEquals

class GameTest {

    @Test
    fun shouldInitGameAndTeams() {
        Game.init(arrayOf(arrayOf(0, 1, 2), arrayOf(0, 1, 3), arrayOf(0, 2, 4)))

        Assert.assertArrayEquals(Game.map?.map, arrayOf(
                arrayOf(MapTile.EMPTY, MapTile.BANANA, MapTile.WALL),
                arrayOf(MapTile.EMPTY, MapTile.BANANA, MapTile.EMPTY),
                arrayOf(MapTile.EMPTY, MapTile.WALL, MapTile.EMPTY))
        )

        assertEquals(Game.countBananas(), 2)

        assertEquals(Game.players?.size, 2)
    }

    @Test
    fun gameShouldNotBeFinished() {
        Game.init(arrayOf(arrayOf(0, 1, 2), arrayOf(0, 1, 2), arrayOf(0, 1, 2)))

        assertEquals(Game.isFinished(), false)
    }

    @Test
    fun gameShouldBeFinished() {
        Game.init(arrayOf(arrayOf(0, 0, 0), arrayOf(0, 0, 0), arrayOf(0, 0, 0)))

        assertEquals(Game.isFinished(), true)
    }

    @Test
    fun shouldReturnMapTile() {
        Game.init(arrayOf(arrayOf(0, 1, 2), arrayOf(0, 1, 3), arrayOf(1, 1, 4)))
        assertEquals(MapTile.BANANA, Game.getMapTileAtPosition(Position(1, 1)))
        assertEquals(MapTile.EMPTY, Game.getMapTileAtPosition(Position(0, 0)))

    }

    @Test
    fun shouldReturnInvalidMapTileForInvalidPosition() {
        Game.init(arrayOf(arrayOf(0, 1, 2), arrayOf(0, 1, 3), arrayOf(1, 1, 4)))
        assertEquals(MapTile.WALL, Game.getMapTileAtPosition(Position(-1, -1)))
    }
}

