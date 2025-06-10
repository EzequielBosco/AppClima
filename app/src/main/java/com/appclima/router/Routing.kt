package com.appclima.router

import android.annotation.SuppressLint
import androidx.navigation.NavHostController

class Routing(
    val navHostController: NavHostController
): Router {
    @SuppressLint("DefaultLocale")
    override fun navigate(route: Route) {
        when(route){
            Route.cities -> navHostController.navigate(route.id)

            is Route.weather -> {
                val route = String.format(format="%s?lat=%f&lon=%f&name=%s",route.id,route.lat,route.lon,route.name)
                navHostController.navigate(route)
            }
        }
    }
}
