package com.appclima.presentation.cities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.appclima.model.City

@Composable
fun CitiesView(
    modifier: Modifier = Modifier,
    state: CitiesState,
    onAction: (CitiesIntent) -> Unit
) {
    var value by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = value,
            onValueChange = {
                value = it
                onAction(CitiesIntent.Search(value))
            },
            label = { Text("Search city") }
        )

        when (state) {
            CitiesState.Loading -> Text("Loading...")
            is CitiesState.Error -> Text("Error: ${state.message}")
            is CitiesState.Result -> CityList(state.cities) { onAction(CitiesIntent.Select(it)) }
            CitiesState.Empty -> Text("No results found")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityList(cities: List<City>, onSelect: (City) -> Unit) {
    LazyColumn {
        items(cities) { city ->
            Card(
                onClick = { onSelect(city) },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "${city.name}, ${city.country}",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
