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
            CitiesScreen(navController)
        }

        composable("weather_screen/{cityName}") { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName")
            WeatherScreen()
        }
    }
}
