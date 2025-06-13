package com.appclima.presentation.cities

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appclima.repository.RepositoryImpl
import com.appclima.router.Navigator

@Composable
fun CitiesPage(navigator: Navigator) {
    val viewModel: CitiesViewModel = viewModel(
        factory = CitiesViewModelFactory(
            repository = RepositoryImpl(),
            router = navigator
        )
    )

    CitiesView(
        state = viewModel.uiState,
        onAction = { viewModel.onIntent(it) }
    )
}
