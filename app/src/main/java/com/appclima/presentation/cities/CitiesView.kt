package com.appclima.presentation.cities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import com.appclima.model.City
import com.istea.appdelclima.presentacion.ciudades.CitiesIntention


@Composable
fun CitiesView (
    modifier: Modifier = Modifier,
    state: CitiesStatus,
    onAction: (CitiesIntention) -> Unit,
    it: City
) {
    var value by remember{ mutableStateOf("") }

    Column(modifier = modifier) {
        TextField(
            value = value,
            label = { Text(text = "buscar por nombre") },
            onValueChange = {
                value = it
                onAction(CitiesIntention.Search(value))
            },
        )
        when(state) {
            CitiesStatus.loading -> Text(text = "cargando")
            is CitiesStatus.error -> Text(text = state.message)
            is CitiesStatus.result -> ListOfCities(state.cities) {
                onAction(
                    CitiesIntention.Select(it)
                )
            }
            CitiesStatus.empty -> Text(text = "No hay resultados")
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ListOfCities(cities: List<City>, onSelect: (City)->Unit) {
        LazyColumn {
            items(items = cities) {
                Card(onClick = { onSelect(it) }) {
                    Text(text = it.name)
                }
            }
        }
    }
}

@Composable
fun ListOfCities(x0: List<City>, content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}
