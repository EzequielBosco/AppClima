package com.appclima.repository

import com.appclima.model.City
import com.appclima.model.Weather
import com.appclima.repository.dtos.ForecastDTO
import com.appclima.repository.dtos.GeoCityDTO
import com.appclima.repository.dtos.ListForecast
import com.appclima.repository.dtos.WeatherDTO
import com.appclima.repository.dtos.toDomain
import com.appclima.repository.dtos.toWeather
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RepositoryImpl : Repository {

    private val apiKey = "1fa4d4cc1c418e346211cf58b0422cd2"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun searchCity(cityName: String): List<City> {
        val response = client.get("https://api.openweathermap.org/geo/1.0/direct") {
            parameter("q", cityName)
            parameter("limit", 100)
            parameter("appid", apiKey)
        }

        return if (response.status == HttpStatusCode.OK) {
            response.body<List<GeoCityDTO>>().mapIndexed { index, dto -> dto.toDomain(index) }
        } else {
            throw Exception("City search failed with status: ${response.status}")
        }
    }

    override suspend fun getCityByCoordinates(lat: Double, lon: Double): City? {
        val response = client.get("https://api.openweathermap.org/geo/1.0/reverse") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("limit", 1)
            parameter("appid", apiKey)
        }

        return if (response.status == HttpStatusCode.OK) {
            val list = response.body<List<GeoCityDTO>>()
            list.firstOrNull()?.toDomain(0)
        } else {
            throw Exception("Reverse geocoding failed with status: ${response.status}")
        }
    }

    override suspend fun getWeather(lat: Float, lon: Float): Weather {
        val response = client.get("https://api.openweathermap.org/data/2.5/weather") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }

        return if (response.status == HttpStatusCode.OK) {
            val dto = response.body<WeatherDTO>()
            dto.toWeather()
        } else {
            throw Exception("Weather fetch failed with status: ${response.status}")
        }
    }

    override suspend fun getForecast(cityName: String): List<ListForecast> {
        val response = client.get("https://api.openweathermap.org/data/2.5/forecast") {
            parameter("q", cityName)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }

        return if (response.status == HttpStatusCode.OK) {
            val dto = response.body<ForecastDTO>()
            dto.list
        } else {
            throw Exception("Forecast fetch failed with status: ${response.status}")
        }
    }
}
