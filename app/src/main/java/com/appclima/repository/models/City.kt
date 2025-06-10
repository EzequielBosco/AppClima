package com.appclima.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int,
    val name: String,
    val country: String,
    val latitude: Float,
    val longitude: Float,
)
