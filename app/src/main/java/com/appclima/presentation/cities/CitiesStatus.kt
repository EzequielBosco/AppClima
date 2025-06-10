package com.appclima.presentation.cities

import com.appclima.model.City

sealed class CitiesStatus {
    data object empty: CitiesStatus()
    data object loading: CitiesStatus()
    data class result( val cities : List<City> ) : CitiesStatus()
    data class error(val message: String): CitiesStatus()
}
