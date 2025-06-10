package com.appclima.repository.models

import com.appclima.model.City
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDTO (
    val cod: String,
    val message: Long,
    val cnt: Long,
    val list: List<ListForecast>,
    val city: City,
)

@Serializable
data class ListForecast(
    val dt: Long,
    val main: MainForecast,
)

@Serializable
data class MainForecast(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Long,
    val sea_level: Long,
    val grnd_level: Long,
    val humidity: Long,
    val temp_kf: Double,
)
