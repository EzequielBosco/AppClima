package com.appclima.presentation.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appclima.repository.Repository
import com.appclima.repository.dtos.toForecastItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: Repository
) : ViewModel() {

    var uiState by mutableStateOf<WeatherState>(WeatherState.Loading)
        private set

    private val _effect = MutableSharedFlow<WeatherEffect>()
    val effect = _effect.asSharedFlow()

    fun handleIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.LoadWeather -> {
                loadWeather(intent.lat, intent.lon, intent.city)
            }

            is WeatherIntent.ShareForecast -> {
                viewModelScope.launch {
                    _effect.emit(WeatherEffect.ShowShareSheet(intent.forecastText))
                }
            }
        }
    }

    private fun loadWeather(lat: Double, lon: Double, cityName: String) {
        viewModelScope.launch {
            try {
                val weather = repository.getWeather(lat.toFloat(), lon.toFloat())

                val rawForecast = repository.getForecast(cityName)
                val forecastItems = rawForecast
                    .map { it.toForecastItem() }
                    .groupBy { it.date }
                    .values
                    .take(5)
                    .map { it.first() }

                uiState = WeatherState.Result(
                    data = weather,
                    forecast = forecastItems
                )

            } catch (e: Exception) {
                uiState = WeatherState.Error("Failed to load weather: ${e.message}")
            }
        }
    }
}

class WeatherViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(repository) as T
    }
}
