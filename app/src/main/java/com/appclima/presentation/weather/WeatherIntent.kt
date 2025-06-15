package com.appclima.presentation.weather

sealed class WeatherIntent {
    data class ShareForecast(val forecastText: String) : WeatherIntent()
    data class LoadWeather(val lat: Double, val lon: Double, val city: String) : WeatherIntent()
}

