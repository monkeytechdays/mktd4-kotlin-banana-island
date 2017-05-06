package io.monkeypatch.mktd4

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("mktd4")
class Mktd4Properties {

    lateinit var backendUrl: String
    lateinit var username: String

}