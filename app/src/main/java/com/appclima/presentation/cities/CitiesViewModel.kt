package com.appclima.presentation.cities

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appclima.model.City
import com.appclima.repository.Repository
import com.appclima.router.AppRoute
import com.appclima.router.Navigator
import kotlinx.coroutines.launch

class CitiesViewModel(
    private val repository: Repository,
    private val router: Navigator
) : ViewModel() {

    var uiState by mutableStateOf<CitiesState>(CitiesState.Empty)
    private var cityList: List<City> = emptyList()

    init {
        loadInitialCities()
    }

    private fun loadInitialCities() {
        uiState = CitiesState.Loading
        viewModelScope.launch {
            try {
                val names = listOf("Buenos Aires", "Paris", "New York")
                val results = names.mapNotNull {
                    repository.searchCity(it).firstOrNull()
                }

                cityList = results.take(3)
                uiState = if (cityList.isEmpty()) CitiesState.Empty else CitiesState.Result(cityList)
            } catch (e: Exception) {
                uiState = CitiesState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun onIntent(intent: CitiesIntent) {
        when (intent) {
            is CitiesIntent.Search -> search(intent.name)
            is CitiesIntent.Select -> select(intent.city)
        }
    }

    private fun search(name: String) {
        uiState = CitiesState.Loading
        viewModelScope.launch {
            try {
                cityList = repository.searchCity(name)
                uiState = if (cityList.isEmpty()) CitiesState.Empty else CitiesState.Result(cityList)
            } catch (e: Exception) {
                uiState = CitiesState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun select(city: City) {
        router.navigate(AppRoute.Weather(city.lat, city.lon, city.name))
    }
}

class CitiesViewModelFactory(
    private val repository: Repository,
    private val router: Navigator
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {
            CitiesViewModel(repository, router) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
