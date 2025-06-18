package com.example.appclima

import com.appclima.presentation.weather.*
import com.appclima.model.Weather
import com.appclima.repository.Repository
import com.appclima.repository.dtos.ForecastMain
import com.appclima.repository.dtos.ForecastWeather
import com.appclima.repository.dtos.ListForecast
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTests {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WeatherViewModel
    private lateinit var repository: Repository

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
    fun returnValidWeather_success_shouldUpdateUiStateToResult() = runTest {
        // Given
        val weather = Weather(
            temperature = 20.0,
            description = "Sunny",
            city = "Buenos Aires",
            iconUrl = "01d",
            humidity = 1
        )

        val forecastList = listOf(
            ListForecast(
                dt = 1718702400, // timestamp de "2024-06-18 12:00:00"
                main = ForecastMain(temp = 22f, temp_min = 20f, temp_max = 24f),
                weather = listOf(ForecastWeather(description = "Clear", icon = "01d"))
            ),
            ListForecast(
                dt = 1718788800,
                main = ForecastMain(temp = 18f, temp_min = 16f, temp_max = 20f),
                weather = listOf(ForecastWeather(description = "Rain", icon = "09d"))
            ),
            ListForecast(
                dt = 1718875200,
                main = ForecastMain(temp = 21f, temp_min = 19f, temp_max = 23f),
                weather = listOf(ForecastWeather(description = "Cloudy", icon = "03d"))
            ),
            ListForecast(
                dt = 1718961600,
                main = ForecastMain(temp = 19f, temp_min = 18f, temp_max = 21f),
                weather = listOf(ForecastWeather(description = "Sunny", icon = "01d"))
            ),
            ListForecast(
                dt = 1719048000,
                main = ForecastMain(temp = 17f, temp_min = 15f, temp_max = 19f),
                weather = listOf(ForecastWeather(description = "Rain", icon = "09d"))
            ),
            // Extra (se debe descartar por lógica de ViewModel que toma solo 5)
            ListForecast(
                dt = 1719134400,
                main = ForecastMain(temp = 23f, temp_min = 22f, temp_max = 25f),
                weather = listOf(ForecastWeather(description = "Sunny", icon = "01d"))
            )
        )

        coEvery { repository.getWeather(10f, 20f) } returns weather
        coEvery { repository.getForecast("Córdoba") } returns forecastList

        // When
        viewModel.handleIntent(WeatherIntent.LoadWeather(10.0, 20.0, "Córdoba"))
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState is WeatherState.Result)
        val result = viewModel.uiState as WeatherState.Result
        assertEquals(weather, result.data)
        assertEquals(5, result.forecast.size) // solo toma 5 días
    }

    @Test
    fun returnFail_shouldUpdateUiStateToError() = runTest {
        // Given
        coEvery { repository.getWeather(any(), any()) } throws RuntimeException("API error")
        coEvery { repository.getForecast(any()) } returns emptyList()

        // When
        viewModel.handleIntent(WeatherIntent.LoadWeather(10.0, 20.0, "Córdoba"))
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState is WeatherState.Error)
        val error = viewModel.uiState as WeatherState.Error
        assertEquals("Failed to load weather: API error", error.message)
    }

    @Test
    fun returnValid_WhenShareForecast_shouldEmitEffect() = runTest {
        // When
        val textToShare = "Sunny today in Córdoba!"
        viewModel.handleIntent(WeatherIntent.ShareForecast(textToShare))
        val effect = viewModel.effect.first()

        // Then
        assertTrue(effect is WeatherEffect.ShowShareSheet)
        assertEquals(textToShare, (effect as WeatherEffect.ShowShareSheet).text)
    }
}
