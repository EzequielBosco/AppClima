package com.appclima.router

interface Router {
    fun navigate(route: Route)
}

sealed class Route(val id: String) {
    data object cities: Route("cities")
    data class weather(val lat: Float,val lon:Float, val name:String): Route("weather")
}
