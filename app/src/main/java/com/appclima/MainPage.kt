package com.appclima

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appclima.presentation.cities.CitiesPage
import com.appclima.router.AppRoute
import com.appclima.router.NavigatorImpl

@Composable
fun MainPage() {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = AppRoute.Cities.path
    ) {
        composable(AppRoute.Cities.path) {
            CitiesPage(navigator = NavigatorImpl(navHostController))
        }
    }
}
