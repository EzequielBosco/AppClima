package com.appclima.repository.dtos

import com.appclima.model.City
import kotlinx.serialization.Serializable

@Serializable
data class GeoCityDTO(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double
)

fun GeoCityDTO.toDomain(id: Int): City {
    return City(id, name, country, lat.toFloat(), lon.toFloat())
}
