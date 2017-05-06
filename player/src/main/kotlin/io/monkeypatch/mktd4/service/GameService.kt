package io.monkeypatch.mktd4.service

import io.monkeypatch.mktd4.ai.IPlayer
import io.monkeypatch.mktd4.model.Game
import io.monkeypatch.mktd4.rest.GameController
import io.monkeypatch.mktd4.rest.Ping
import io.monkeypatch.mktd4.rest.PlayerClient
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class GameService : ApplicationRunner {

    private val LOG = LogFactory.getLog(GameService::class.java)

    @Autowired
    lateinit var player: IPlayer

    @Autowired
    lateinit var playerClient: PlayerClient

    @Autowired
    private val appContext: ApplicationContext? = null

    @Autowired
    lateinit var playerController: PlayerClient

    override fun run(args: ApplicationArguments?) {
        // Register user
        val mono = playerController.registerUser()
        mono.subscribe(
                { (id) ->
                    Game.currentUserId = id
                    LOG.info("Player registered")
                }
                ,{ err ->
                    initiateShutdown(-1)
                    LOG.warn("Player registered ($err)-> stop")
                }
        )
    }

    fun initiateShutdown(returnCode: Int) {
        SpringApplication.exit(appContext!!, ExitCodeGenerator { returnCode })
    }

    fun startGame(game: io.monkeypatch.mktd4.rest.Map) {
        Game.init(game.map)
    }

    fun play(ping: Ping, roundId: String) {
        // Apply other players moves
        Game.applyMoves(*ping.moves.map { Pair(it.userId, it.move) }.toTypedArray())

        // Play and apply move to game
        player.play(Game).let {

            LOG.info("Local player moved to $it")
            val mono = playerClient.play(roundId, it)
            mono.subscribe(
                    { success ->
                        if (success.is2xxSuccessful) {
                            Game.applyMoves(Pair(ping.currentUserId, it))
                        } else {
                            LOG.warn("Received error code $success")
                        }
                    }
                    , { err ->
                LOG.warn("Error playing game $err. retry ?")
            })
        }
    }

    fun endGame() {
        initiateShutdown(-1)
    }

}