package com.appclima.presentation.weather

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.appclima.repository.RepositoryImpl
import com.appclima.router.Navigator
import kotlinx.coroutines.flow.collectLatest

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

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is WeatherEffect.ShowShareSheet -> {
                    shareText(context, effect.text)
                }
            }
        }
    }

    LaunchedEffect(key1 = lat to lon to name) {
        if (lat != null && lon != null) {
            viewModel.handleIntent(WeatherIntent.LoadWeather(lat.toDouble(), lon.toDouble(), name))
        }
    }

    if (lat != null && lon != null) {
        WeatherView(
            state = viewModel.uiState,
            cityName = name,
            navigator = navigator,
            onShareClick = { forecastText ->
                viewModel.handleIntent(WeatherIntent.ShareForecast(forecastText))
            }
        )
    } else {
        Text("Error parsing location data", color = MaterialTheme.colorScheme.error)
    }
}

fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(intent, "Share weather forecast")
    context.startActivity(chooser)
}
