package io.monkeypatch.mktd4.rest

import io.monkeypatch.mktd4.Mktd4Properties
import io.monkeypatch.mktd4.model.Move
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.net.InetAddress

@Component
class PlayerClient(val properties: Mktd4Properties) {

    @Autowired
    private lateinit var serverProperties: ServerProperties

    private val webClient: WebClient by lazy { WebClient.create() }


    private data class Player(val name: String, val endpoint: String)
    private data class Round(val move: String)



    fun registerUser(): Mono<UserId> {
        return webClient.post()
                .uri("${this.properties.backendUrl}/player")
                .body(BodyInserters.fromObject(Player(properties.username, "${InetAddress.getLocalHost().hostAddress}:${serverProperties.port}")))
                .exchange()
                .flatMap { r -> r.bodyToMono(UserId::class.java) }
    }

    fun play(requestUuid: String, move: Move): Mono<HttpStatus> {
        return webClient.post()
                .uri("${this.properties.backendUrl}/map")
                .header("uuid", requestUuid)
                .body(BodyInserters.fromObject(Round(move.id)))
                .exchange()
                .map(ClientResponse::statusCode)
    }

}