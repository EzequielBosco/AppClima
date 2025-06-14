package com.appclima.presentation.cities

import com.appclima.model.City

sealed class CitiesIntent {
    data class Search(val name: String) : CitiesIntent()
    data class Select(val city: City) : CitiesIntent()
    object UseLocation : CitiesIntent()
}
