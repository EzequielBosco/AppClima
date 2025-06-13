package com.appclima.repository.dtos

import com.appclima.model.Weather

fun WeatherDTO.toWeather(): Weather {
    val description = weather.firstOrNull()?.description ?: "No description"
    val icon = weather.firstOrNull()?.icon
    val iconUrl = icon?.let { "https://openweathermap.org/img/wn/${it}@2x.png" }

    return Weather(
        city = name,
        temperature = main.temp.toDouble(),
        description = description,
        iconUrl = iconUrl
    )
}
