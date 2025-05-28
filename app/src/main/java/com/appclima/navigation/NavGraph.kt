package com.appclima.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.appclima.ui.screens.CitiesScreen
import com.appclima.ui.screens.WeatherScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.CitiesScreen.route
    ) {
        composable(Routes.CitiesScreen.route) {
            CitiesScreen()
        }

        composable("weather_screen/{cityName}") { backStackEntry ->
            // Esta línea queda solo si más adelante usás cityName en algún ViewModel
            val cityName = backStackEntry.arguments?.getString("cityName")

            // WeatherScreen no acepta parámetros, así que se llama así
            WeatherScreen()
        }
    }
}
