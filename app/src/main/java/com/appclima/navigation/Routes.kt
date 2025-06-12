package com.appclima.navigation

sealed class Routes(val route: String) {
    data object CitiesScreen : Routes("cities_screen")
    data object WeatherScreen : Routes("weather_screen/{cityName}") {
        fun createRoute(cityName: String) = "weather_screen/$cityName"
    }
}

