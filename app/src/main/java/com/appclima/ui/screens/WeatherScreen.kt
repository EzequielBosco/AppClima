package com.appclima.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appclima.model.Weather

@Composable
fun WeatherScreen(
    weather: Weather? = null,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Clima actual", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> CircularProgressIndicator()

            errorMessage != null -> {
                Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Reintentar")
                }
            }

            weather != null -> {
                Text(text = "Ciudad: ${weather.city}")
                Text(text = "Temperatura: ${weather.temperature}°C")
                Text(text = "Descripción: ${weather.description}")
            }

            else -> {
                Text("No hay datos disponibles.")
            }
        }
    }
}
