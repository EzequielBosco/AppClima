package com.appclima.router

import androidx.navigation.NavHostController

interface Navigator {
    fun navigate(to: AppRoute)
}

class NavigatorImpl(
    private val navController: NavHostController
) : Navigator {
    override fun navigate(to: AppRoute) {
        navController.navigate(to.path)
    }
}
