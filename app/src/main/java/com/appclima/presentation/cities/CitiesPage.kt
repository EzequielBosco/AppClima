package com.appclima.presentation.cities

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appclima.location.DefaultLocationProvider
import com.appclima.repository.RepositoryImpl
import com.appclima.router.Navigator

@Composable
fun CitiesPage(navigator: Navigator) {
    val context = LocalContext.current

    var hasLocationPermission by rememberSaveable { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasLocationPermission = granted }
    )

    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
            hasLocationPermission = true
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val viewModel: CitiesViewModel = viewModel(
        factory = CitiesViewModelFactory(
            repository = RepositoryImpl(),
            router = navigator,
            locationProvider = DefaultLocationProvider(context)
        )
    )

    CitiesView(
        state = viewModel.uiState,
        onAction = viewModel::onIntent
    )
}
