package com.appclima.presentation.cities

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.appclima.repository.repositoryApi
import com.appclima.router.Routing

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
