package com.appclima.model

data class Weather(
    val city: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val iconUrl: String? = null
)
