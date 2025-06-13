package com.example.appclima.viewmodel

import com.appclima.model.City
import com.appclima.mock.CitiesViewModelMock
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CitiesViewModelMockTest {

    private lateinit var viewModel: CitiesViewModelMock

    @Before
    fun setup() {
        viewModel = CitiesViewModelMock()
    }

    @Test
    fun `loadCities should populate cities list and update isLoading`() = runTest {
        // Al inicio no hay ciudades ni loading
        assertTrue(viewModel.cities.isEmpty())
        assertFalse(viewModel.isLoading)

        // Ejecutamos la carga
        viewModel.loadCities()

        // Verificamos estados luego de la carga
        assertEquals(3, viewModel.cities.size)
        assertTrue(viewModel.cities.contains(City("Buenos Aires")))
        assertTrue(viewModel.cities.contains(City("Córdoba")))
        assertTrue(viewModel.cities.contains(City("Rosario")))

        // Verificamos que isLoading haya vuelto a false
        assertFalse(viewModel.isLoading)
    }
}
