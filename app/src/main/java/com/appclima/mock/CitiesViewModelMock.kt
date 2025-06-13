package com.appclima.mock

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.appclima.model.CityDto

class CitiesViewModelMock : ViewModel() {
    var cities = mutableStateListOf<CityDto>()
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadCities() {
        isLoading = true
        // Simulación de carga
        cities.clear()
        cities.addAll(
            listOf(
                CityDto("Buenos Aires"),
                CityDto("Córdoba"),
                CityDto("Rosario")
            )
        )
        isLoading = false
    }
}
