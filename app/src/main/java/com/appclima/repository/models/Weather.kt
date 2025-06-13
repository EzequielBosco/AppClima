package com.appclima.model

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val city: String,
    val temperature: Double,
    val description: String,
    val iconUrl: String? = null
)
