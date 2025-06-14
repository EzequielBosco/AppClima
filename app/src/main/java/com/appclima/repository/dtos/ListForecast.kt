package com.appclima.repository.dtos

import com.appclima.model.ForecastItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ListForecast.toForecastItem(): ForecastItem {
    val date = Date(this.dt * 1000L)
    val dateString = SimpleDateFormat("EEE dd", Locale.getDefault()).format(date)


    return ForecastItem(
        date = dateString,
        temperature = main.temp.toDouble(),
        description = weather.firstOrNull()?.description ?: "No data",
        iconCode = weather.firstOrNull()?.icon ?: ""
    )
}
