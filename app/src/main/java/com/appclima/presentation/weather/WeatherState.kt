package com.appclima.presentation.weather

import com.appclima.model.Weather
import com.appclima.model.ForecastItem

sealed interface WeatherState {
    object Loading : WeatherState
    data class Error(val message: String) : WeatherState
    data class Result(
        val data: Weather,
        val forecast: List<ForecastItem>
    ) : WeatherState
}
