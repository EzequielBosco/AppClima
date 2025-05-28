package com.example.appclima

import app.cash.turbine.test
import com.appclima.viewmodel.CitiesViewModel
import com.appclima.model.City
import com.appclima.repository.CityRepositoryMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class CitiesViewModelTest {

    private lateinit var viewModel: CitiesViewModel

    @Before
    fun setup() {
        val repository = CityRepositoryMock()
        viewModel = CitiesViewModel(repository)
    }

    @Test
    fun `should load cities successfully`() = runTest {
        viewModel.state.test {
            val emission = awaitItem()
            assertEquals(false, emission.isLoading)
            assert(emission.cities.isNotEmpty())
        }
    }
}
