package com.appclima.presentation.cities

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appclima.location.LocationProvider
import com.appclima.model.City
import com.appclima.repository.Repository
import com.appclima.router.AppRoute
import com.appclima.router.Navigator
import kotlinx.coroutines.launch

// ==== UnitTest (Lean) ====
// Esta clase evita que los unit test utilicen tools de android sdk. (no afecta test integrado)
object SafeLog {
    var enabled: Boolean = true

    fun d(tag: String, msg: String) {
        if (enabled) Log.d(tag, msg)
    }

    fun e(tag: String, msg: String, throwable: Throwable? = null) {
        if (enabled) Log.e(tag, msg, throwable)
    }

    fun w(tag: String, msg: String) {
        if (enabled) Log.w(tag, msg)
    }
}

class CitiesViewModel(
    private val repository: Repository,
    private val router: Navigator,
    private val locationProvider: LocationProvider
) : ViewModel() {

    var uiState by mutableStateOf<CitiesState>(CitiesState.Empty)
    private var cityList: List<City> = emptyList()

    var selectedCityName by mutableStateOf("")
        private set

    init {
        loadInitialCities()
    }

    private fun loadInitialCities() {
        uiState = CitiesState.Loading
        viewModelScope.launch {
            try {
                val names = listOf("Buenos Aires", "Paris", "New York", "Berlin", "Madrid")
                val results = names.mapNotNull {
                    repository.searchCity(it).firstOrNull()
                }

                cityList = results.take(5)
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
            CitiesIntent.UseLocation -> useLocation()
        }
    }

    private fun search(name: String) {
        val query = name.trim()
        if (query.isBlank()) {
            uiState = CitiesState.Empty
            return
        }

        selectedCityName = query
        uiState = CitiesState.Loading
        viewModelScope.launch {
            try {
                cityList = repository.searchCity(query)
                uiState = if (cityList.isEmpty()) CitiesState.Empty else CitiesState.Result(cityList)
            } catch (e: Exception) {
                uiState = CitiesState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun select(city: City) {
        selectedCityName = city.name

        SafeLog.d("CitiesViewModel", "Selected city: ${city.name}, ${city.country}")

        viewModelScope.launch {
            try {
                router.navigate(AppRoute.Weather(city.lat, city.lon, city.name))
            } catch (e: Exception) {
                SafeLog.e("CitiesViewModel", "Navigation failed: ${e.message}", e)
                uiState = CitiesState.Error("Error navigating to weather screen: ${e.message}")
            }
        }
    }

    private fun useLocation() {
        uiState = CitiesState.Loading
        viewModelScope.launch {
            try {
                SafeLog.d("CitiesViewModel", "Requesting location from provider...")
                val location = locationProvider.getCurrentLocation()

                if (location != null) {
                    val (lat, lon) = location
                    SafeLog.d("CitiesViewModel", "Got location: lat=$lat, lon=$lon")

                    val city = repository.getCityByCoordinates(lat, lon)
                    if (city != null) {
                        SafeLog.d("CitiesViewModel", "City found: ${city.name}, ${city.country}")
                        selectedCityName = city.name

                        cityList = repository.searchCity(city.name)
                        uiState = if (cityList.isEmpty()) CitiesState.Empty else CitiesState.Result(cityList)
                    } else {
                        SafeLog.w("CitiesViewModel", "No city found for coordinates")
                        uiState = CitiesState.Empty
                    }
                } else {
                    SafeLog.w("CitiesViewModel", "Location is null")
                    uiState = CitiesState.Error("Location not available")
                }
            } catch (e: Exception) {
                SafeLog.e("CitiesViewModel", "Error retrieving location: ${e.message}", e)
                uiState = CitiesState.Error("Error retrieving location: ${e.message}")
            }
        }
    }
}

class CitiesViewModelFactory(
    private val repository: Repository,
    private val router: Navigator,
    private val locationProvider: LocationProvider
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {
            CitiesViewModel(repository, router, locationProvider) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
