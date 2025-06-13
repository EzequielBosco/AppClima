package com.appclima.router

import android.net.Uri

sealed interface AppRoute {
    val path: String

    object Cities : AppRoute {
        override val path = "cities"
    }

    data class Weather(
        val latitude: Float,
        val longitude: Float,
        val city: String
    ) : AppRoute {
        override val path = "weather?lat=$latitude&lon=$longitude&name=${Uri.encode(city)}"
    }
}
