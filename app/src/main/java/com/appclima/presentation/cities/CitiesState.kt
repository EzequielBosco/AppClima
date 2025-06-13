package com.appclima.presentation.cities

import com.appclima.model.City

sealed class CitiesState {
    data object Empty : CitiesState()
    data object Loading : CitiesState()
    data class Result(val cities: List<City>) : CitiesState()
    data class Error(val message: String) : CitiesState()
}
