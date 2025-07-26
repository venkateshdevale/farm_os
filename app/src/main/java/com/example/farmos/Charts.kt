package com.example.farmos.ui.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*

/**
 * MPLineChart - Used for displaying trends (e.g., price or yield) with smooth curve and fill
 */
@Composable
fun MPLineChart(data: List<Float>, color: Int) {
    val entries = data.mapIndexed { index, value -> Entry(index.toFloat() + 1, value) }
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                val dataSet = LineDataSet(entries, "").apply {
                    this.color = color
                    valueTextColor = Color.BLACK
                    lineWidth = 2f
                    setCircleColor(color)
                    circleRadius = 4f
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawFilled(true)
                    fillAlpha = 80
                    fillColor = color
                }

                this.data = LineData(dataSet)
                this.description = Description().apply { text = "" }
                this.animateX(800)
                axisRight.isEnabled = false
                legend.isEnabled = false
                xAxis.setDrawGridLines(false)
                xAxis.textColor = Color.BLACK
                axisLeft.textColor = Color.BLACK
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        update = { chart -> chart.invalidate() }
    )
}

/**
 * MPBarChart - Used for displaying categorical data like disease frequency or yield comparisons
 */
@Composable
fun MPBarChart(data: List<Float>) {
    val entries = data.mapIndexed { index, value -> BarEntry(index.toFloat() + 1, value) }
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                val dataSet = BarDataSet(entries, "Data").apply {
                    color = Color.rgb(76, 175, 80) // Agriculture green
                    valueTextColor = Color.BLACK
                    valueTextSize = 12f
                }
                this.data = BarData(dataSet)
                this.description = Description().apply { text = "" }
                this.animateY(800)
                axisRight.isEnabled = false
                legend.isEnabled = false
                xAxis.setDrawGridLines(false)
                xAxis.textColor = Color.BLACK
                axisLeft.textColor = Color.BLACK
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        update = { chart -> chart.invalidate() }
    )
}

/**
 * MPPieChart - Used for displaying percentage distribution like rainfall or crop share
 */
@Composable
fun MPPieChart(data: List<Float>) {
    val entries = data.mapIndexed { index, value ->
        PieEntry(value, "Year ${index + 1}")
    }
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                val dataSet = PieDataSet(entries, "").apply {
                    colors = listOf(
                        Color.rgb(33, 150, 243),  // Blue
                        Color.rgb(255, 193, 7),   // Yellow
                        Color.rgb(76, 175, 80),   // Green
                        Color.rgb(244, 67, 54)    // Red
                    )
                    valueTextColor = Color.BLACK
                    valueTextSize = 12f
                }
                this.data = PieData(dataSet)
                this.description = Description().apply { text = "" }
                this.isDrawHoleEnabled = true
                this.holeRadius = 50f
                this.transparentCircleRadius = 55f
                this.animateY(1000)
                legend.isEnabled = false
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        update = { chart -> chart.invalidate() }
    )
}
