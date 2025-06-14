package com.appclima.presentation.weather

import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.appclima.repository.RepositoryImpl
import com.appclima.router.Navigator

@Composable
fun WeatherPage(
    backStackEntry: NavBackStackEntry,
    navigator: Navigator
) {
    val lat = backStackEntry.arguments?.getFloat("lat")
    val lon = backStackEntry.arguments?.getFloat("lon")
    val name = Uri.decode(backStackEntry.arguments?.getString("name") ?: "")

    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(RepositoryImpl())
    )

    LaunchedEffect(key1 = lat to lon to name) {
        if (lat != null && lon != null) {
            viewModel.loadWeather(lat.toDouble(), lon.toDouble(), name)
        }
    }

    if (lat != null && lon != null) {
        WeatherView(
            state = viewModel.uiState,
            cityName = name,
            navigator = navigator
        )
    } else {
        Text("Error parsing location data", color = MaterialTheme.colorScheme.error)
    }
}
