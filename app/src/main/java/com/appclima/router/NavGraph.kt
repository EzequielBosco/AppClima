package com.appclima.router

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.appclima.presentation.cities.CitiesPage
import com.appclima.router.AppRoute
import com.appclima.router.Navigator

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
    }
}
