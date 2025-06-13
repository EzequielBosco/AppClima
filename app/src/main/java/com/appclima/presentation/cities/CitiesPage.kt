package com.appclima.presentation.cities

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.appclima.model.City
import com.appclima.router.Routing
import com.istea.appdelclima.repository.repositoryApi

@Composable
fun CitiesPage(
    navHostController:  NavHostController
) {
    val viewModel : CitiesViewModel = viewModel(
        factory = CitiesViewModelFactory(
            repository = repositoryApi(),
            router = Routing(navHostController)
        )
    )
    CitiesView(
        state = viewModel.uiState,
        onAction = { intention ->
            viewModel.exec(intention)
        }
    )
}
