package com.appclima.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appclima.viewmodel.CitiesViewModel

@Composable
fun CitiesScreen(viewModel: CitiesViewModel = CitiesViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = { viewModel.loadCities() }) {
            Text("Cargar ciudades")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            Text("Cargando...")
        } else {
            viewModel.cities.forEach { city ->
                Text(text = "Ciudad: ${city.name}")
            }
        }
    }
}
