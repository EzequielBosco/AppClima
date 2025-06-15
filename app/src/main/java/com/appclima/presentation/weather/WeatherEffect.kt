package com.appclima.presentation.weather

sealed class WeatherEffect {
    data class ShowShareSheet(val text: String) : WeatherEffect()
}