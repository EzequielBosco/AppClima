package com.appclima.ui.screens

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.appclima.presentation.cities.CitiesView
import com.appclima.presentation.cities.CitiesViewModel
import com.appclima.presentation.cities.CitiesViewModelFactory
import com.appclima.router.Routing
import com.istea.appdelclima.repository.repositoryApi

@Composable
fun CitiesScreen(navController: NavHostController) {
    val viewModel: CitiesViewModel = viewModel(
        factory = CitiesViewModelFactory(
            repository = repositoryApi(),
            router = Routing(navController)
        )
    )

    CitiesView(
        state = viewModel.uiState,
        onAction = { viewModel.exec(it) }
    )
}
