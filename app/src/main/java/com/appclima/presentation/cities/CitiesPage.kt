package com.appclima.presentation.cities

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.appclima.router.Routing
import com.istea.appdelclima.repository.RepositorioApi

@Composable
fun CitiesPage(
    navHostController:  NavHostController
) {
    val viewModel : CitiesViewModel = viewModel(
        factory = CitiesViewModelFactory(
            repo = RepositorioApi(),
            router = Routing(navHostController)
        )
    )
    CitiesView(
        state = viewModel.uiState,
        onAction = { intention ->
            viewModel.(intention)
        },
        it = it
    )
}
