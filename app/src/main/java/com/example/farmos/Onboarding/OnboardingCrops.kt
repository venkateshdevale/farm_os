package com.example.farmos.ui.screens.onboarding

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.farmos.data.CropSuggestion
import com.example.farmos.data.mockCropSuggestions
import com.example.farmos.nav
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import androidx.compose.ui.viewinterop.AndroidView
import com.example.farmos.Data.OnboardingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingCropSuggestScreen(navController: NavController, onboardingViewModel : OnboardingViewModel) {
    var selectedCrop by remember { mutableStateOf<CropSuggestion?>(null) }
    var infoCrop by remember { mutableStateOf<CropSuggestion?>(null) }
    Log.d("Shabaz", onboardingViewModel.toString())
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEFF8E2), Color(0xFFDDE9C7))
                )
            )
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD8F5DC)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Farm details", fontWeight = FontWeight.Bold, color = Color(0xFF2F4D1D))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("District: ${onboardingViewModel.district}")
                    Text("State: ${onboardingViewModel.state}")
                    Text("Area: ${"%.2f".format(onboardingViewModel.areaInAcres)} acres")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Best Crops for Your Farm",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F4D1D)
            )

            Spacer(modifier = Modifier.height(8.dp))
            mockCropSuggestions.forEach { crop ->
                CropCard(
                    crop = crop,
                    isSelected = selectedCrop?.name == crop.name,
                    onSelect = {
                        selectedCrop = crop
                        infoCrop = crop
                    }
                )
            }
        }

        // Bottom sheet with charts & proceed
        infoCrop?.let { crop ->
            ModalBottomSheet(
                onDismissRequest = { infoCrop = null },
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CropInfoContent(
                        crop = crop,
                        onProceed = {
                            infoCrop = null
                            navController.navigate(nav.onboardingConfirm)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CropCard(
    crop: CropSuggestion,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFD8F5DC) else Color(0xFFF5F5DC)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = crop.iconUrl,
                contentDescription = crop.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(crop.name, fontWeight = FontWeight.Bold)
                Text("Yield: ${crop.expectedYieldTonPerAcre} ton/acre")
                Text("Price: ₹${crop.expectedPricePerTon}/ton")
            }

            IconButton(onClick = { /* Mic later */ }) {
                Icon(Icons.Default.Mic, contentDescription = "Mic", tint = Color(0xFFD4AF37))
            }
        }
    }
}

@Composable
fun CropInfoContent(crop: CropSuggestion, onProceed: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(crop.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(crop.reason, color = Color(0xFF4C4C4C))

        Text("Historical Trends", fontWeight = FontWeight.SemiBold)

        // Area Chart for Price
        Text("Price Trend (₹/ton)", fontWeight = FontWeight.SemiBold)
        MPLineChart(crop.historicalTrend.map { it.pricePerTon.toFloat() }, android.graphics.Color.rgb(244, 180, 0))

        // Donut Chart for Rainfall
        Text("Rainfall Distribution (%)", fontWeight = FontWeight.SemiBold)
        MPPieChart(crop.historicalTrend.map { it.rainfallMm.toFloat() })

        // Bar Chart for Yield
        Text("Yield Trend (ton/acre)", fontWeight = FontWeight.SemiBold)
        MPBarChart(crop.historicalTrend.map { it.yieldTonPerAcre.toFloat() })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onProceed,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
        ) {
            Text("Proceed", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MPLineChart(data: List<Float>, color: Int) {
    val entries = data.mapIndexed { index, value -> Entry(index.toFloat() + 1, value) }
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                val dataSet = LineDataSet(entries, "").apply {
                    this.color = color
                    valueTextColor = android.graphics.Color.BLACK
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
                xAxis.textColor = android.graphics.Color.BLACK
                axisLeft.textColor = android.graphics.Color.BLACK
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        update = { chart -> chart.invalidate() }
    )
}

@Composable
fun MPBarChart(data: List<Float>) {
    val entries = data.mapIndexed { index, value -> BarEntry(index.toFloat() + 1, value) }
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                val dataSet = BarDataSet(entries, "Yield").apply {
                    color = android.graphics.Color.rgb(76, 175, 80)
                    valueTextColor = android.graphics.Color.BLACK
                }
                this.data = BarData(dataSet)
                this.description = Description().apply { text = "" }
                this.animateY(800)
                axisRight.isEnabled = false
                legend.isEnabled = false
                xAxis.setDrawGridLines(false)
                xAxis.textColor = android.graphics.Color.BLACK
                axisLeft.textColor = android.graphics.Color.BLACK
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        update = { chart -> chart.invalidate() }
    )
}

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
                        android.graphics.Color.rgb(33, 150, 243),
                        android.graphics.Color.rgb(255, 193, 7),
                        android.graphics.Color.rgb(76, 175, 80),
                        android.graphics.Color.rgb(244, 67, 54)
                    )
                    valueTextColor = android.graphics.Color.BLACK
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
