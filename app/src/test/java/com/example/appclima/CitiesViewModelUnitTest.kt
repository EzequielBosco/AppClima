package com.example.appclima

import com.appclima.location.LocationProvider
import com.appclima.model.City
import com.appclima.repository.Repository
import com.appclima.router.AppRoute
import com.appclima.router.Navigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import com.appclima.presentation.cities.CitiesIntent
import com.appclima.presentation.cities.CitiesState
import com.appclima.presentation.cities.CitiesViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CitiesViewModelUnitTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CitiesViewModel
    private lateinit var fakeRepository: FakeRepository
    private lateinit var fakeNavigator: FakeNavigator
    private lateinit var fakeLocationProvider: FakeLocationProvider

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        fakeRepository = FakeRepository()
        fakeNavigator = FakeNavigator()
        fakeLocationProvider = FakeLocationProvider()
        viewModel = CitiesViewModel(fakeRepository, fakeNavigator, fakeLocationProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search with valid city name returns result`() = runTest {
        // given
        val city = City(1, "Volga", "DE", 1f, 1f)
        fakeRepository.searchResults = listOf(city)

        // when
        viewModel.onIntent(CitiesIntent.Search("Volga"))
        advanceUntilIdle()

        // then
        assertTrue(viewModel.uiState is CitiesState.Result)
        val result = (viewModel.uiState as CitiesState.Result)
        assertEquals(1, result.cities.size)
        assertEquals("Volga", result.cities.first().name)
    }

    @Test
    fun `search with blank name returns Empty state`() = runTest {
        // when
        viewModel.onIntent(CitiesIntent.Search("   "))
        advanceUntilIdle()

        // then
        assertTrue(viewModel.uiState is CitiesState.Empty)
    }

    @Test
    fun `search throws exception returns Error state`() = runTest {
        // given
        fakeRepository.throwOnSearch = true

        // when
        viewModel.onIntent(CitiesIntent.Search("Paris"))
        advanceUntilIdle()

        // then
        assertTrue(viewModel.uiState is CitiesState.Error)
    }
/*
    @Test
    fun `select city triggers navigation`() = runTest {
        // given
        val city = City(1, "Madrid", "ES", 1f, 1f)

        // when
        viewModel.onIntent(CitiesIntent.Select(city))
        advanceUntilIdle()

        // then
        val route = fakeNavigator.lastRoute as? AppRoute.Weather
        assertNotNull(route)
        assertEquals(city.name, route?.city)
        assertEquals(city.lat, route?.latitude)
        assertEquals(city.lon, route?.longitude)
    }

    @Test
    fun `select city handles navigation error`() = runTest {
        // given
        val city = City(1, "Madrid", "ES", 1f, 1f)
        fakeNavigator.throwOnNavigate = true

        // when
        viewModel.onIntent(CitiesIntent.Select(city))
        advanceUntilIdle()

        // then
        assertTrue(viewModel.uiState is CitiesState.Error)
        assertTrue((viewModel.uiState as CitiesState.Error).message.contains("Error navigating"))
    }

    @Test
    fun `use location returns matching city`() = runTest {
        // given
        val city = City(1, "Buenos Aires", "AR", -34f, -58f)
        fakeLocationProvider.location = Pair(-34.0, -58.0)
        fakeRepository.cityByCoords = city
        fakeRepository.searchResults = listOf(city)

        // when
        viewModel.onIntent(CitiesIntent.UseLocation)
        advanceUntilIdle()

        // then
        assertTrue(viewModel.uiState is CitiesState.Result)
        val result = (viewModel.uiState as CitiesState.Result)
        assertEquals("Buenos Aires", result.cities.first().name)
    }

    @Test
    fun `use location returns error if null`() = runTest {
        // given
        fakeLocationProvider.location = null

        // when
        viewModel.onIntent(CitiesIntent.UseLocation)
        advanceUntilIdle()

        // then
        assertTrue(viewModel.uiState is CitiesState.Error)
        assertEquals("Location not available", (viewModel.uiState as CitiesState.Error).message)
    }

    @Test
    fun `use location returns empty when no city found`() = runTest {
        // given
        fakeLocationProvider.location = Pair(0.0, 0.0)
        fakeRepository.cityByCoords = null

        // when
        viewModel.onIntent(CitiesIntent.UseLocation)
        advanceUntilIdle()

        // then
        assertTrue(viewModel.uiState is CitiesState.Empty)
    }

    @Test
    fun `use location throws exception returns error`() = runTest {
        // given
        fakeLocationProvider.throwOnRequest = true

        // when
        viewModel.onIntent(CitiesIntent.UseLocation)
        advanceUntilIdle()

        // then
        assertTrue(viewModel.uiState is CitiesState.Error)
        assertTrue((viewModel.uiState as CitiesState.Error).message.contains("Error retrieving location"))
    }
*/
    // ==== Fakes ====

    private class FakeRepository : Repository {
        var searchResults: List<City> = emptyList()
        var throwOnSearch = false
        var cityByCoords: City? = null

        override suspend fun searchCity(cityName: String): List<City> {
            if (throwOnSearch) throw RuntimeException("Search failed")
            return searchResults
        }

        override suspend fun getCityByCoordinates(lat: Double, lon: Double): City? {
            return cityByCoords
        }

        override suspend fun getWeather(lat: Float, lon: Float) = TODO("Not used in this ViewModel")

        override suspend fun getForecast(cityName: String) = emptyList<com.appclima.repository.dtos.ListForecast>()
    }

    private class FakeNavigator : Navigator {
        var lastRoute: AppRoute? = null
        var throwOnNavigate = false

        override fun navigate(route: AppRoute) {
            if (throwOnNavigate) throw RuntimeException("Navigation error")
            lastRoute = route
        }
    }

    private class FakeLocationProvider : LocationProvider {
        var location: Pair<Double, Double>? = null
        var throwOnRequest = false

        override suspend fun getCurrentLocation(): Pair<Double, Double>? {
            if (throwOnRequest) throw RuntimeException("Location error")
            return location
        }
    }
}
