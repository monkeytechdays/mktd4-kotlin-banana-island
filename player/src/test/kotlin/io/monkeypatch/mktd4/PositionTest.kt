package io.monkeypatch.mktd4

import io.monkeypatch.mktd4.model.Move
import io.monkeypatch.mktd4.model.Position
import io.monkeypatch.mktd4.model.plus
import org.junit.Test
import kotlin.test.assertEquals

class PositionTest {

    @Test
    fun positionCanBeInvalid() {
        assertEquals(Position(-3, -1), Position(-1, -1))
        assertEquals(Position() + Move.WEST, Position(-1, 0))
        assertEquals(Position() + Move.NORTH, Position(0, -1))
    }

    @Test
    fun positionShouldMoveToEast() {
        assertEquals(Position() + Move.EAST, Position(1, 0))
    }

    @Test
    fun positionShouldMoveToWest() {
        assertEquals(Position(1,0) + Move.WEST, Position(0, 0))
    }

    @Test
    fun positionShouldMoveToSouth() {
        assertEquals(Position() + Move.SOUTH, Position(0, 1))
    }

    @Test
    fun positionShouldMoveToNorth() {
        assertEquals(Position(0,1) + Move.NORTH, Position(0, 0))
    }

}

