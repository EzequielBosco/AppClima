package com.appclima.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val name: String,
    val country: String,
    val lat: Float,
    val lon: Float,
)
