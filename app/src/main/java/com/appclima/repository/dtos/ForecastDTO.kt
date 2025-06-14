package com.appclima.repository.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ForecastDTO(
    val list: List<ListForecast>
)

@Serializable
data class ListForecast(
    val dt: Long,
    val main: ForecastMain,
    val weather: List<ForecastWeather>
)

@Serializable
data class ForecastMain(
    val temp: Float
)

@Serializable
data class ForecastWeather(
    val description: String,
    val icon: String
)
