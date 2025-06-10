package com.appclima.presentation.cities

import com.appclima.viewmodel.CitiesViewModel

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.istea.appdelclima.repository.RepositorioApi
import com.istea.appdelclima.router.Enrutador

@Composable
fun CitiesPage(
    navHostController:  NavHostController
) {
    val viewModel : CitiesViewModel = viewModel(
        factory = CitiesViewModelFactory(
            repo = RepositorioApi(),
            router = Enrutador(navHostController)
        )
    )
    CitiesView(
        state = viewModel.uiState,
        onAction = { intention ->
            viewModel.(intention)
        }
    )
}
