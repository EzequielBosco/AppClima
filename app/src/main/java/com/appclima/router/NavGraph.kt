package com.appclima.router

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.appclima.presentation.cities.CitiesPage
import com.appclima.presentation.weather.WeatherPage


@Composable
fun NavGraph(
    navController: NavHostController,
    navigator: Navigator
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Cities.path
    ) {
        composable(AppRoute.Cities.path) {
            CitiesPage(navigator = navigator)
        }

        composable(
            route = "weather?lat={lat}&lon={lon}&name={name}", // Include arguments in the route string
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType }, // Change to FloatType
                navArgument("lon") { type = NavType.FloatType }, // Change to FloatType
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            WeatherPage(backStackEntry = backStackEntry, navigator = navigator)
        }
    }
}
