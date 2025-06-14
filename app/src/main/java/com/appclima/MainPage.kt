package com.appclima

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.appclima.router.NavGraph
import com.appclima.router.NavigatorImpl

@Composable
fun MainPage() {
    val navHostController = rememberNavController()
    val navigator = NavigatorImpl(navHostController)

    NavGraph(
        navController = navHostController,
        navigator = navigator
    )
}
