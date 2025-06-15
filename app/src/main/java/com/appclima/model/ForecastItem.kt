package com.appclima.model

data class ForecastItem(
    val date: String,
    val temperature: Double,
    val description: String,
    val iconCode: String,
    val tempMin: Float,
    val tempMax: Float,
)
