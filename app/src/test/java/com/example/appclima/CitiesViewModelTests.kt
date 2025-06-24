package com.example.appclima

import android.util.Log
import com.appclima.router.Navigator
import com.appclima.location.LocationProvider
import com.appclima.model.City
import com.appclima.presentation.cities.CitiesIntent
import com.appclima.presentation.cities.CitiesState
import com.appclima.presentation.cities.CitiesViewModel
import com.appclima.repository.Repository
import com.appclima.router.AppRoute
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CitiesViewModelTests {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CitiesViewModel
    private lateinit var router: Navigator
    private var locationProvider: LocationProvider = mockk(relaxed = true)
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        repository = mockk()
        router = mockk(relaxed = true) // No testea navegación
        locationProvider = mockk()

        // Mock de android.util.Log
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any<String>(), any<String>()) } returns 0
        every { Log.w(any<String>(), any<Throwable>()) } returns 0

        viewModel = CitiesViewModel(repository, router, locationProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun returnValid_5InitialCities() = runTest {
        // When
        val cities = listOf(
            City(1, "Buenos Aires", "AR", 1f, 1f),
            City(2, "Paris", "FR", 2f, 2f),
            City(3, "New York", "US", 3f, 3f),
            City(4, "Berlin", "DE", 4f, 4f),
            City(5, "Madrid", "ES", 5f, 5f)
        )

        // Mock para cada búsqueda inicial
        cities.forEach {
            coEvery { repository.searchCity(it.name) } returns listOf(it)
        }

        viewModel = CitiesViewModel(repository, router, locationProvider)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState is CitiesState.Result)
        val result = viewModel.uiState as CitiesState.Result
        assertTrue("Deben haber 5 ciudades", result.cities.size == 5)
    }

    @Test
    fun returnValid_ExistingCity() = runTest {
        // Given
        val city = City(1, "Buenos Aires", "AR", -34f, -58f)
        coEvery { repository.searchCity("Buenos Aires") } returns listOf(city)

        // When
        viewModel.onIntent(CitiesIntent.Search("Buenos Aires"))
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState is CitiesState.Result)
        val result = viewModel.uiState as CitiesState.Result
        assertTrue("Debe haber al menos 1 ciudad en el resultado", result.cities.isNotEmpty())
    }

    @Test
    fun returnValid_WhenFoundCity() = runTest {
        val expectedCity = City(id = 1, name = "Córdoba", country = "AR", lat = 10.0f, lon = 20.0f)
        coEvery { repository.searchCity("cordoba") } returns listOf(expectedCity)

        viewModel.onIntent(CitiesIntent.Search("cordoba"))
        advanceUntilIdle()

        assertTrue(viewModel.uiState is CitiesState.Result)
        val result = viewModel.uiState as CitiesState.Result
        assertTrue("Debe haber al menos 1 ciudad en el resultado", result.cities.isNotEmpty())
        assertEquals(CitiesState.Result(listOf(expectedCity)), viewModel.uiState)
    }

    @Test
    fun returnEmpty_WhenNotFoundCity() = runTest {
        coEvery { repository.searchCity("zzzz") } returns emptyList()

        viewModel.onIntent(CitiesIntent.Search("zzzz"))
        advanceUntilIdle()

        assertEquals(CitiesState.Empty, viewModel.uiState)
    }

    @Test
    fun returnError_NotException() = runTest {
        coEvery { repository.searchCity("error") } throws RuntimeException("Falla")

        viewModel.onIntent(CitiesIntent.Search("error"))
        advanceUntilIdle()

        assertEquals(CitiesState.Error("Falla"), viewModel.uiState)
    }

    @Test
    fun returnValid_WhenFoundLocation() = runTest {
        val city = City(id = 1, name = "Buenos Aires", country = "AR", lat = -34.0f, lon = -58.0f)

        coEvery { locationProvider.getCurrentLocation() } returns Pair(-34.0, -58.0)
        coEvery { repository.getCityByCoordinates(-34.0, -58.0) } returns city
        coEvery { repository.searchCity("Buenos Aires") } returns listOf(city)

        viewModel.onIntent(CitiesIntent.UseLocation)
        advanceUntilIdle()

        assertEquals(CitiesState.Result(listOf(city)), viewModel.uiState)
    }

    @Test
    fun returnError_WhenNotFoundLocation() = runTest {
        coEvery { locationProvider.getCurrentLocation() } returns null

        viewModel.onIntent(CitiesIntent.UseLocation)
        advanceUntilIdle()

        assertEquals(CitiesState.Error("Location not available"), viewModel.uiState)
    }

    @Test
    fun returnValid_WhenRouteWorks() = runTest {
        val city = City(id = 1, name = "Buenos Aires", country = "AR", lat = -34.0f, lon = -58.0f)

        var rutaNavegada: AppRoute? = null
        val fakeNavigator = object : Navigator {
            override fun navigate(to: AppRoute) {
                rutaNavegada = to
            }
        }

        // Paso fakeNavigator en viewModel
        viewModel = CitiesViewModel(repository, fakeNavigator, locationProvider)

        viewModel.onIntent(CitiesIntent.Select(city))
        advanceUntilIdle()

        val ruta = rutaNavegada as? AppRoute.Weather
        assertNotNull("Debe navegar a la ruta Weather", ruta)
        assertEquals(city.lat, ruta?.latitude)
        assertEquals(city.lon, ruta?.longitude)
        assertEquals(city.name, ruta?.city)
    }

    @Test // lean 1
    fun returnError_WhenNavigationFails() = runTest {
        // Given
        val city = City(id = 1, name = "Paris", country = "FR", lat = 48.85f, lon = 2.35f)
        val failingNavigator = object : Navigator {
            override fun navigate(to: AppRoute) {
                throw RuntimeException("Navigation failed")
            }
        }

        viewModel = CitiesViewModel(repository, failingNavigator, locationProvider)

        // When
        viewModel.onIntent(CitiesIntent.Select(city))
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState is CitiesState.Error)
        val errorState = viewModel.uiState as CitiesState.Error
        assertTrue(errorState.message.contains("Navigation failed"))
    }


    @Test // lean 2
    fun returnEmpty_WhenValidCoordinatesButNoCityFound() = runTest {
        // Given
        coEvery { locationProvider.getCurrentLocation() } returns Pair(0.0, 0.0)
        coEvery { repository.getCityByCoordinates(0.0, 0.0) } returns null

        // When
        viewModel.onIntent(CitiesIntent.UseLocation)
        advanceUntilIdle()

        // Then
        assertEquals(CitiesState.Empty, viewModel.uiState)
    }

    @Test // lean 3
    fun returnEmpty_WhenCityFoundButSearchReturnsEmpty() = runTest {
        // Given
        val exampleCity = City(id = 99, name = "Example City", country = "EC", lat = 0.0f, lon = 0.0f)
        coEvery { locationProvider.getCurrentLocation() } returns Pair(0.0, 0.0)
        coEvery { repository.getCityByCoordinates(0.0, 0.0) } returns exampleCity
        coEvery { repository.searchCity("Example City") } returns emptyList()

        // When
        viewModel.onIntent(CitiesIntent.UseLocation)
        advanceUntilIdle()

        // Then
        assertEquals(CitiesState.Empty, viewModel.uiState)
    }

    @Test //lean 4
    fun returnError_WhenLocationProviderFails() = runTest {
        // Given
        coEvery { locationProvider.getCurrentLocation() } throws RuntimeException("Location service error")

        // When
        viewModel.onIntent(CitiesIntent.UseLocation)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState is CitiesState.Error)
        val error = viewModel.uiState as CitiesState.Error
        assertTrue(error.message.contains("Location service error"))
    }

    @Test //lean 5
    fun returnError_WhenRepositoryGetCityByCoordinatesFails() = runTest {
        // Given
        coEvery { locationProvider.getCurrentLocation() } returns Pair(1.0, 2.0)
        coEvery { repository.getCityByCoordinates(1.0, 2.0) } throws RuntimeException("Repo error")

        // When
        viewModel.onIntent(CitiesIntent.UseLocation)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState is CitiesState.Error)
        val error = viewModel.uiState as CitiesState.Error
        assertTrue(error.message.contains("Repo error"))
    }




}