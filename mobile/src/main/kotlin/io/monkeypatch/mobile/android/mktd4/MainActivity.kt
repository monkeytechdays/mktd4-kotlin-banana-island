package io.monkeypatch.mobile.android.mktd4

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import io.monkeypatch.mktd4.api.Mktd4API
import io.monkeypatch.mktd4.model.Game
import io.monkeypatch.mktd4.model.BoardTiles
import io.monkeypatch.mktd4.model.MapTile
import io.monkeypatch.mktd4.model.Player
import io.monkeypatch.mobile.android.mktd4.view.ScoreRecyclerAdapter
import io.monkeypatch.mobile.android.mktd4.view.TileView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "MainActivity"
    }

    val mapView: TileView by lazy { findViewById(R.id.map) as TileView }
    val scoreView: RecyclerView  by lazy { findViewById(R.id.recycler) as RecyclerView }
    lateinit var scoreAdapter: ScoreRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreView.layoutManager = LinearLayoutManager(applicationContext)
        scoreAdapter = ScoreRecyclerAdapter()
        scoreView.adapter = scoreAdapter

        RxReplace()

    }

    fun RxReplace() {
        Observable.interval(0, 500, TimeUnit.MILLISECONDS)
                .flatMap { queryGame() }
                .retry(10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    Log.i(TAG, "refreshing data")
                    mapView.map = result.map.map
                    scoreAdapter.items = result.players
                },
                        { throwable -> Log.e(TAG, "Unable to get data:" + throwable) })
    }

    fun queryGame(): Observable<Game> {
        return Mktd4API.getMap().map {
            //BoardTiles
            val gameMap = BoardTiles(it.map.map { it.map { MapTile.from(it, true) }.toTypedArray() }.toTypedArray())
            val teams = it.gamers.map { gamer -> gameMap.locateFirst(gamer.id)?.let { Player(gamer.id - MapTile.WALL.value, gamer.name, it, gamer.score) } }.filterNotNull().toTypedArray()
            Game(gameMap, teams)
        }.toObservable()
    }
}
