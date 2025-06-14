// NavigatorImpl.kt
package com.appclima.router

import android.annotation.SuppressLint
import android.net.Uri
import androidx.navigation.NavHostController

class NavigatorImpl(
    private val navController: NavHostController
) : Navigator {

    @SuppressLint("DefaultLocale")
    override fun navigate(to: AppRoute) {
        when (to) {
            is AppRoute.Cities -> navController.navigate(to.path)

            is AppRoute.Weather -> {
                // Force to Float and then toString() to ensure no Double precision issues
                // Using .toFloat() is only necessary if to.latitude and to.longitude were Double
                // However, since you said "all float", this line is mostly for demonstrating explicit control.
                val latString = to.latitude.toString()
                val lonString = to.longitude.toString()
                val route = "weather?lat=${latString}&lon=${lonString}&name=${Uri.encode(to.city)}"
                navController.navigate(route)
            }
        }
    }
}
