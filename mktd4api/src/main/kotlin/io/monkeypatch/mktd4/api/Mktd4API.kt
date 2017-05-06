package io.monkeypatch.mktd4.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET

const val MKTD_4_URL: String = "http://192.168.2.4:8090/"

// Rest Model
data class Gamer(@JsonProperty("id") val id: Int, @JsonProperty("name") val name: String, @JsonProperty("score") val score: Int)

data class Map(@JsonProperty("map") val map: Array<Array<Int>>, @JsonProperty("gamers") val gamers: List<Gamer>)

// Rest api
interface MapApi {
    @GET("/map")
    fun getMap(): Single<io.monkeypatch.mktd4.api.Map>

}

object Mktd4API {

    private val mapApi: MapApi
    private val objectMapper: ObjectMapper = ObjectMapper()

    init {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val retrofitQivivo = Retrofit.Builder()
                .baseUrl(MKTD_4_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        mapApi = retrofitQivivo.create(MapApi::class.java)
    }

    fun getMap(): Single<io.monkeypatch.mktd4.api.Map> = mapApi.getMap()
}