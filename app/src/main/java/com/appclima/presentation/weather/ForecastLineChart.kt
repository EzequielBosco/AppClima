package com.appclima.presentation.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appclima.model.ForecastItem
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.marker.markerComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.legend.horizontalLegend
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes.pillShape
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.legend.LegendItem


@Composable
fun ForecastLineChart(
    forecast: List<ForecastItem>,
    modifier: Modifier = Modifier
) {
    if (forecast.isEmpty()) return

    val maxTemps = forecast.mapIndexed { index, item ->
        FloatEntry(index.toFloat(), item.tempMax)
    }

    val minTemps = forecast.mapIndexed { index, item ->
        FloatEntry(index.toFloat(), item.tempMin)
    }

    val model = entryModelOf(maxTemps, minTemps)

    val marker = markerComponent(
        label = textComponent(),
        indicator = ShapeComponent(pillShape, color = Color.Blue.hashCode()),
        guideline = LineComponent(
            color = Color.LightGray.hashCode(),
            thicknessDp = 1f
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 8.dp, end = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 6.dp, bottom = 4.dp, start = 3.dp, end = 3.dp)
        ) {
            Text(
                text = "Temperature Trend",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )
            Chart(
                chart = lineChart(
                    lines = listOf(
                        lineSpec(
                            lineColor = Color(0xFF1E88E5),
                            lineThickness = 3.dp,
                            lineBackgroundShader = null,
                            point = null,
                            pointSize = 4.dp,
                            dataLabel = null,
                        ),
                        lineSpec(
                            lineColor = Color(0xFF90CAF9),
                            lineThickness = 3.dp,
                            lineBackgroundShader = null,
                            point = null,
                            pointSize = 4.dp,
                            dataLabel = null,
                        )
                    )
                ),
                model = model,
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = { value, _ ->
                        forecast.getOrNull(value.toInt())?.date?.take(3) ?: ""
                    },
                    itemPlacer = com.patrykandpatrick.vico.core.axis.AxisItemPlacer.Horizontal.default(
                        spacing = 1
                    )
                ),
                marker = marker,
                legend = horizontalLegend(
                    items = listOf(
                        LegendItem(
                            icon = ShapeComponent(color = 0xFF1E88E5.toInt()),
                            label = TextComponent.Builder().build(),
                            labelText = "Max Temp"
                        ),
                        LegendItem(
                            icon = ShapeComponent(color = 0xFF90CAF9.toInt()),
                            label = TextComponent.Builder().build(),
                            labelText = "Min Temp"
                        )
                    ),
                    iconSize = 12.dp,
                    iconPadding = 6.dp,
                    spacing = 12.dp,
                    lineSpacing = 4.dp
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .height(290.dp)
            )
        }
    }
}
