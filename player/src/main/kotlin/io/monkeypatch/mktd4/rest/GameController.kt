package io.monkeypatch.mktd4.rest

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.monkeypatch.mktd4.ai.IPlayer
import io.monkeypatch.mktd4.extension.asString
import io.monkeypatch.mktd4.model.Game
import io.monkeypatch.mktd4.model.Move
import io.monkeypatch.mktd4.service.GameService
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.IOException

@RestController
class GameController {

    private val LOG = LogFactory.getLog(GameController::class.java)

    @Autowired
    lateinit var gameService : GameService

    @PostMapping("/map")
    fun initGame(@RequestBody game: Map) {
        LOG.info("InitGame")
        gameService.startGame(game)
    }

    @DeleteMapping("/map")
    fun endGame() {
        LOG.info("End Game!")
        gameService.endGame()
    }

    @PostMapping("/map/{uuid}")
    fun play(@RequestBody ping: Ping, @PathVariable uuid: String) {
        LOG.info("Received : $ping")
        gameService.play(ping, uuid)
    }
}

// Rest Model
class Map(@JsonProperty("map") val map: Array<Array<Int>>)

class Ping(@JsonProperty("id") val currentUserId: Int,
           @JsonProperty("moves") @JsonDeserialize(using = MoveDeserializer::class) val moves: List<UserMove>){

    override fun toString(): String {
        return "Ping(currentUserId=$currentUserId, moves=${moves.asString()})"
    }
}

class UserMove(@JsonProperty("id") val userId: Int, val move: Move){

    override fun toString(): String {
        return "UserMove(userId=$userId, move=$move)"
    }
}

data class UserId(@JsonProperty("id") val id:Int)

// Model Serializer/ Deserialize

class MoveDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<List<UserMove>>(vc) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): List<UserMove> {
        val node = jp.codec.readTree<JsonNode>(jp)
        return node.elements().asSequence().map { deserialize(it) }.filterNotNull().toList()
    }

    fun deserialize(node: JsonNode): UserMove? {
        return UserMove(node.get("id").asInt(), Move.from(node.get("move").asText()))
    }
}
