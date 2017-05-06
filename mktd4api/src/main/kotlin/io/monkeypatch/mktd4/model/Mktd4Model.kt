package io.monkeypatch.mktd4.model

import io.monkeypatch.mktd4.extension.toTypedArray

data class Player(val id: Int, val name: String, var pos: Position, var score: Int = 0)

data class Position(private val _posX: Int = 0, private val _posY: Int = 0) {

    constructor(position: Pair<Int, Int>) : this(position.first, position.second)

    companion object {
        const val INVALID = -1
    }

    var posX = if (_posX >= 0) _posX else INVALID
        set(value) = if (value >= 0) field = value else field = 0

    var posY = if (_posY >= 0) _posY else INVALID
        set(value) = if (value >= 0) field = value else field = 0

    fun isValid() = posX >= 0 && posY >= 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Position

        if (posX != other.posX) return false
        if (posY != other.posY) return false

        return true
    }
}

enum class MapTile(val value: Int) {
    EMPTY(0),
    BANANA(1),
    WALL(2),
    MONKEY_1(3),
    MONKEY_2(4),
    MONKEY_3(5),
    MONKEY_4(6);

    companion object {
        fun from(value: Int, allowMonkey: Boolean): MapTile {
            return if (!allowMonkey && value > WALL.value) EMPTY
            else MapTile.values().firstOrNull { it.value == value } ?: EMPTY
        }

        fun size() = values().size

    }
}

data class BoardTiles(val map: Array<Array<MapTile>>) {

    companion object {
        fun from(mapInt: Array<Array<Int>>): BoardTiles =TODO()
    }

    fun contains(tile: MapTile) = TODO()

    fun none(tile: MapTile) = TODO()

    fun count(tile: MapTile) = TODO()

}

data class Game(val map: BoardTiles, val players: Array<Player>)
