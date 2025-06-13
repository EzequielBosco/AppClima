package com.appclima.presentation.cities

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appclima.model.City
import com.appclima.repository.Repository
import com.appclima.router.Route
import com.appclima.router.Router
import com.istea.appdelclima.presentacion.ciudades.CitiesIntention
import kotlinx.coroutines.launch

class CitiesViewModel(
    val repository: Repository,
    val router: Router
) : ViewModel(){

    var uiState by mutableStateOf<CitiesStatus>(CitiesStatus.empty)
    var ciudades : List<City> = emptyList()

    init {
        cargarCiudadesIniciales()
    }

    private fun cargarCiudadesIniciales() {
        uiState = CitiesStatus.loading
        viewModelScope.launch {
            try {
                val listaCiudades = mutableListOf<City>()
                val nombres = listOf("Buenos Aires", "Paris", "New York") // Ciudades precargadas

                for (nombre in nombres) {
                    val resultado = repository.searchCity(nombre)
                    if (resultado.isNotEmpty()) {
                        listaCiudades.add(resultado[0])
                    }
                }

                ciudades = listaCiudades.take(3)
                if (ciudades.isEmpty()) {
                    uiState = CitiesStatus.empty
                } else {
                    uiState = CitiesStatus.result(ciudades)
                }
            } catch (ex: Exception) {
                uiState = CitiesStatus.error(ex.message ?: "Error desconocido")
            }
        }
    }
    fun exec(intention: CitiesIntention){
        when(intention){
            is CitiesIntention.Search -> buscar(nombre = intention.name)
            is CitiesIntention.Select -> select(city = intention.city)
        }
    }

    private fun buscar( nombre: String){

        uiState = CitiesStatus.loading
        viewModelScope.launch {
            try {
                ciudades = repository.searchCity(nombre)
                if (ciudades.isEmpty()) {
                    uiState = CitiesStatus.empty
                } else {
                    uiState = CitiesStatus.result(ciudades)
                }
            } catch (exeption: Exception){
                uiState = CitiesStatus.error(exeption.message ?: "unknown error")
            }
        }
    }

    private fun select(city: City){
        val route = Route.weather(
            lat = city.lat,
            lon = city.lon,
            name = city.name
        )
        router.navigate(route)
    }
}


class CitiesViewModelFactory(
    private val repository: Repository,
    private val router: Router
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {
            return CitiesViewModel(repository,router) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}