package com.appclima

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.appclima.presentation.cities.CitiesPage
import com.appclima.router.Route


@Composable
fun MainPage() {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = Route.cities.id
    ) {
        composable(
            route = Route.cities.id
        ) {
            CitiesPage(navHostController)
        }

    }
}