package com.appclima.repository

import com.appclima.model.City
import com.appclima.model.Weather
import com.appclima.repository.dtos.ListForecast

interface Repository {
    suspend fun searchCity(ciudad: String): List<City>
    suspend fun getWeather(lat: Float, lon: Float): Weather
    suspend fun getForecast(name: String): List<ListForecast>
    suspend fun getCityByCoordinates(lat: Double, lon: Double): City?
}
