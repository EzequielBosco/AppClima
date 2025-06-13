package com.appclima.repository.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class WeatherDTO(
    val name: String,
    val weather: List<WeatherDescription>,
    val main: MainWeatherData
)

@Serializable
data class MainWeatherData(
    val temp: Float,
    @SerialName("feels_like") val feelsLike: Float,
    @SerialName("temp_min") val tempMin: Float,
    @SerialName("temp_max") val tempMax: Float,
    val pressure: Int,
    val humidity: Int
)
