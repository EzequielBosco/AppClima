package com.appclima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.appclima.model.City

class CitiesViewModel : ViewModel() {
    var cities = mutableStateListOf<City>()
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadCities() {
        isLoading = true
        // Simulación de carga
        cities.clear()
        cities.addAll(
            listOf(
                City("Buenos Aires"),
                City("Córdoba"),
                City("Rosario")
            )
        )
        isLoading = false
    }
}
