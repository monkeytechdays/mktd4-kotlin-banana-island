package io.monkeypatch.mktd4

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(Mktd4Properties::class)
class PlayerApplication

fun main(args: Array<String>) {
    SpringApplication.run(PlayerApplication::class.java, *args)
}





