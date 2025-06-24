package com.example.appclima

import app.cash.turbine.test
import com.appclima.model.Weather
import com.appclima.presentation.weather.WeatherEffect
import com.appclima.presentation.weather.WeatherIntent
import com.appclima.presentation.weather.WeatherState
import com.appclima.presentation.weather.WeatherViewModel
import com.appclima.repository.Repository
import com.appclima.repository.dtos.ForecastMain
import com.appclima.repository.dtos.ForecastWeather
import com.appclima.repository.dtos.ListForecast
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelUnitTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: Repository
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = mockk()
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid weather and forecast, when loading, then should emit Result state`() = runTest {
        // Given
        val weather = Weather(
            temperature = 25.0,
            description = "Clear sky",
            city = "Galicia",
            iconUrl = "01d",
            humidity = 50
        )

        val forecastList = listOf(
            ListForecast(
                dt = 1718702400,
                main = ForecastMain(temp = 20f, temp_min = 18f, temp_max = 22f),
                weather = listOf(ForecastWeather(description = "Clear", icon = "01d"))
            )
        )

        coEvery { repository.getWeather(any(), any()) } returns weather
        coEvery { repository.getForecast("Galicia") } returns forecastList

        // When
        viewModel.handleIntent(WeatherIntent.LoadWeather(40.0, -3.7, "Galicia"))
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState is WeatherState.Result)
        val result = viewModel.uiState as WeatherState.Result
        assertEquals(weather, result.data)
        assertEquals(1, result.forecast.size)

        coVerify { repository.getForecast("Galicia") }
    }


    @Test
    fun `given valid weather and empty forecast, when loading, then should emit Result state with empty forecast`() = runTest {
        // Given
        val weather = Weather(
            temperature = 18.0,
            description = "Cloudy",
            city = "Valencia",
            iconUrl = "02d",
            humidity = 70
        )

        coEvery { repository.getWeather(any(), any()) } returns weather
        coEvery { repository.getForecast("Valencia") } returns emptyList()

        // When
        viewModel.handleIntent(WeatherIntent.LoadWeather(41.3, 2.1, "Valencia"))
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState is WeatherState.Result)
        val result = viewModel.uiState as WeatherState.Result
        assertEquals(weather, result.data)
        assertTrue(result.forecast.isEmpty())

        coVerify { repository.getForecast("Valencia") }
    }


    @Test
    fun `given exception from repository, when loading, then should emit Error state`() = runTest {
        // Given
        coEvery { repository.getWeather(any(), any()) } throws RuntimeException("Network error")
        coEvery { repository.getForecast(any()) } returns emptyList()

        // When
        viewModel.handleIntent(WeatherIntent.LoadWeather(0.0, 0.0, "Example City"))
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState is WeatherState.Error)
        val error = viewModel.uiState as WeatherState.Error
        assertEquals("Failed to load weather: Network error", error.message)
    }


    @Test
    fun `given ShareForecast intent, when handled, then should emit ShowShareSheet effect`() = runTest {
        // Given
        val forecastText = "It'll be sunny tomorrow!"

        // When
        viewModel.handleIntent(WeatherIntent.ShareForecast(forecastText))

        // Then
        viewModel.effect.test {
            val emitted = awaitItem()
            assertTrue(emitted is WeatherEffect.ShowShareSheet)
            assertEquals(forecastText, (emitted as WeatherEffect.ShowShareSheet).text)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
