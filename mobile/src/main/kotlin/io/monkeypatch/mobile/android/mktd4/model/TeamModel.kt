package io.monkeypatch.mobile.android.mktd4.model

import io.monkeypatch.mktd4.model.MapTile
import io.monkeypatch.mktd4.model.Player
import io.monkeypatch.mobile.android.mktd4.R

fun MapTile.getResource(): Int = when (this) {
    MapTile.EMPTY -> R.drawable.map_empty
    MapTile.WALL -> R.drawable.map_wall
    MapTile.BANANA -> R.drawable.map_banana
    MapTile.MONKEY_1 -> R.drawable.monkey1
    MapTile.MONKEY_2 -> R.drawable.monkey2
    MapTile.MONKEY_3 -> R.drawable.monkey3
    MapTile.MONKEY_4 -> R.drawable.monkey4
}

fun Player.getMonkey(): MapTile {
    return MapTile.from(id + MapTile.WALL.value, true)
}


