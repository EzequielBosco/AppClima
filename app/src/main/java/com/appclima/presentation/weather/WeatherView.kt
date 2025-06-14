package com.appclima.presentation.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appclima.router.AppRoute
import com.appclima.router.Navigator


@Composable
fun WeatherView(
    state: WeatherState,
    cityName: String,
    navigator: Navigator
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = cityName,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            WeatherState.Loading -> CircularProgressIndicator()

            is WeatherState.Error -> Text(
                text = state.message,
                color = MaterialTheme.colorScheme.error
            )

            is WeatherState.Result -> {
                val weather = state.data
                val forecast = state.forecast

                Text(
                    text = weather.description.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${weather.temperature}°C",
                    style = MaterialTheme.typography.displayMedium
                )

                Spacer(modifier = Modifier.height(32.dp))

                ForecastChart(forecast = forecast)

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { navigator.navigate(AppRoute.Cities) }
                    ) {
                        Text("Change City")
                    }

                    Button(
                        onClick = {
                            println("Share weather for $cityName")
                        }
                    ) {
                        Text("Share")
                    }
                }
            }
        }
    }
}
