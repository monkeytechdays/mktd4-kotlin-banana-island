package io.monkeypatch.mktd4.model


object Game {
    var map: BoardTiles? = null
    var currentUserId: Int? = null
    var players: List<Player>? = null

    fun init(initMap: Array<Array<Int>>) {
        this.map = io.monkeypatch.mktd4.model.BoardTiles.from(initMap)
        this.players = initMap.mapIndexed { posY, m ->
            m.mapIndexed { posX, tileId ->
                if (tileId >= MapTile.MONKEY_1.value) Player(tileId, "Player $tileId", Position(posX, posY)) else null
            }.filterNotNull()
        }.flatMap { it }.toList()
    }

    fun isFinished() = map?.none(MapTile.BANANA) ?: false
    fun countBananas() = map?.count(MapTile.BANANA) ?: 0
    fun getTeam(id: Int?) = if (id == null) null else players?.firstOrNull { it.id == id }
    fun currentTeam() = getTeam(currentUserId)

    fun merge(): List<List<Any>> {
        return map!!.map.asSequence().mapIndexed { posY, line ->
            line.mapIndexed { posX, v ->
                players?.firstOrNull { it.pos.equals(Position(posX, posY)) }?.name ?: v
            }.toList()
        }.toList()
    }

    fun print(title: String) = "$title => ${println(merge().toString())}"

    fun canMove(player: Player?, move: Move): Boolean {
        return if (player != null && map != null) map!!.canMove(player.pos, move) else false
    }

    fun getMapTileAtPosition(position: Position): MapTile = map?.getMapTileAtPosition(position) ?: MapTile.WALL

    /**
     * Apply moves to map
     * @param moves : Pair of team ID and Move
     */
    fun applyMoves(vararg moves: Pair<Int, Move>) {
        moves.forEach {
            val team = getTeam(it.first)
            if (canMove(team, it.second)) {
                val newPos = team!!.pos + it.second
                // Replace new team position with empty Tile
                val previousTile = map?.replaceTile(newPos, MapTile.EMPTY)
                // Update team position
                team.pos = newPos
                if (previousTile == MapTile.BANANA) {
                    team.score += 1
                }
            }
        }
    }
}

enum class Move(val id: String) {
    NORTH("N"),
    SOUTH("S"),
    WEST("W"),
    EAST("E"),
    NONE("O");

    companion object {
        fun from(value: String) = Move.values().firstOrNull { it.id == value } ?: NONE
    }

    fun opposite() = when(this){
        NORTH -> SOUTH
        SOUTH -> WEST
        WEST -> EAST
        EAST -> WEST
        NONE -> NONE
    }
}

// Position Extensions
operator fun Position.plus(move: Move): Position {
    return when (move) {
        Move.WEST -> Position(posX - 1, posY)
        Move.EAST -> Position(posX + 1, posY)
        Move.NORTH -> Position(posX, posY - 1)
        Move.SOUTH -> Position(posX, posY + 1)
        Move.NONE -> Position(posX, posY)
    }
}

// BoardTiles Extensions
fun BoardTiles.replaceTile(pos: Position, tile: MapTile): MapTile? {
    if (pos.isValid() && map.size > pos.posY) {
        val line = map[pos.posY]
        if (line.size > pos.posX) {
            val previous = line[pos.posX]
            line[pos.posX] = tile
            return previous
        }
    }
    return null
}

fun BoardTiles.getMapTileAtPosition(pos: Position): MapTile {
    if (pos.isValid() && map.size > pos.posY) {
        val line = map[pos.posY]
        if (line.size > pos.posX) {
            return line[pos.posX]
        }
    }
    return MapTile.WALL
}

fun BoardTiles.canMove(pos: Position, move: Move) = pos.isValid() && getMapTileAtPosition(pos + move) != MapTile.WALL