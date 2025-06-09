package com.appclima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

import com.appclima.model.City

class CitiesViewModel : ViewModel() {

    private val _cities =   MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadCities() {
        viewModelScope.launch {
            _isLoading.value = true

            // Simulación de carga
            delay(1000)
            _cities.value = listOf(
                City("CABA"),
                City("La Matanza"),
                City("Vicente López")
            )

            _isLoading.value = false
        }
    }
}