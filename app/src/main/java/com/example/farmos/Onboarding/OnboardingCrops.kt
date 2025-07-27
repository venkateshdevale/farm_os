package com.example.farmos.ui.screens.onboarding

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.farmos.data.CropSuggestion
import com.example.farmos.data.mockCropSuggestions
import com.example.farmos.nav
import com.example.farmos.Data.OnboardingViewModel
import com.example.farmos.ui.components.MPBarChart
import com.example.farmos.ui.components.MPLineChart
import com.example.farmos.ui.components.MPPieChart
import com.example.farmos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingCropSuggestScreen(
    navController: NavController,
    onboardingViewModel: OnboardingViewModel
) {
    val dmSans = FontFamily(Font(R.font.dm_sans))
    var selectedCrop by remember { mutableStateOf<CropSuggestion?>(null) }
    var infoCrop by remember { mutableStateOf<CropSuggestion?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
    ) {
        // Main content
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(bottom = 74.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Progress/instruction section, empathy and clarity
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE4FFD7)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Text(
                    text = "Step 3 of 3",
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF20521F),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                Text(
                    text = "Based on your land, FarmOS and DhartiMitra have found crops that give you max yield and minimum headache. Tap any crop to see the real story, not just random advice.",
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 2.dp)
                )
            }

            // Farm summary card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD8F5DC)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Your Farm", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2F4D1D), fontFamily = dmSans)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("District: ${onboardingViewModel.district}", fontFamily = dmSans, fontSize = 13.sp)
                    Text("State: ${onboardingViewModel.state}", fontFamily = dmSans, fontSize = 13.sp)
                    Text("Area: ${"%.2f".format(onboardingViewModel.areaInAcres)} acres", fontFamily = dmSans, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Recommended Crops For Basappa",
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = Color(0xFF00695C)
            )
            Text(
                text = "These are not just random picks! These crops are matched for YOUR land, your district's climate, and real price/yield trends. Tap each to see why.",
                fontFamily = dmSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )

            // Crop suggestions
            mockCropSuggestions.forEach { crop ->
                CropCard(
                    crop = crop,
                    isSelected = selectedCrop?.name == crop.name,
                    onSelect = {
                        selectedCrop = crop
                        infoCrop = crop
                    },
                    dmSans = dmSans
                )
            }

            // If nothing selected, show subtle prompt
            if (selectedCrop == null) {
                Text(
                    text = "Tip: Tap a crop to check the real market and weather data behind the suggestion.",
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }
        }

        // Fixed Proceed Button at bottom of screen
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Button(
                onClick = {
                    selectedCrop?.let {
                        onboardingViewModel.farmName = it.name
                        navController.navigate(nav.onboardingConfirm)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                enabled = selectedCrop != null
            ) {
                Text(
                    text = if (selectedCrop != null) "Proceed" else "Select a Crop to Continue",
                    fontFamily = dmSans,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }

        // Bottom sheet: crop details and proceed
        infoCrop?.let { crop ->
            ModalBottomSheet(
                onDismissRequest = { infoCrop = null },
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                CropInfoContent(
                    crop = crop,
                    onProceed = {
                        infoCrop = null
                        onboardingViewModel.farmName = crop.name
                        navController.navigate(nav.onboardingConfirm)
                    },
                    dmSans = dmSans
                )
            }
        }
    }
}

@Composable
fun CropCard(
    crop: CropSuggestion,
    isSelected: Boolean,
    onSelect: () -> Unit,
    dmSans: FontFamily
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
                    .size(62.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(crop.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = dmSans)
                Text("Yield: ${crop.expectedYieldTonPerAcre} ton/acre", fontFamily = dmSans, fontSize = 12.sp)
                Text("Avg. Price: ₹${crop.expectedPricePerTon}/ton", fontFamily = dmSans, fontSize = 12.sp)
            }
            IconButton(onClick = { /* Mic for voice coming soon */ }) {
                Icon(Icons.Default.Mic, contentDescription = "Mic", tint = Color(0xFFD4AF37))
            }
        }
    }
}

@Composable
fun CropInfoContent(crop: CropSuggestion, onProceed: () -> Unit, dmSans: FontFamily) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Text(
            crop.name,
            fontFamily = dmSans,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp,
            color = Color(0xFF2F4D1D)
        )
        Text(
            crop.reason,
            fontFamily = dmSans,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            color = Color(0xFF4C4C4C)
        )

        Text("Historical Market Trends", fontWeight = FontWeight.SemiBold, color = Color(0xFF7A6630), fontFamily = dmSans, fontSize = 13.sp)

        // Area Chart for Price
        Text("Market Price Trend (₹/ton)", fontWeight = FontWeight.SemiBold, fontFamily = dmSans, fontSize = 13.sp)
        MPLineChart(crop.historicalTrend.map { it.pricePerTon.toFloat() }, android.graphics.Color.rgb(244, 180, 0))

        // Donut Chart for Rainfall
        Text("Rainfall Over Years (%)", fontWeight = FontWeight.SemiBold, fontFamily = dmSans, fontSize = 13.sp)
        MPPieChart(crop.historicalTrend.map { it.rainfallMm.toFloat() })

        // Bar Chart for Yield
        Text("Yield Trend (ton/acre)", fontWeight = FontWeight.SemiBold, fontFamily = dmSans, fontSize = 13.sp)
        MPBarChart(crop.historicalTrend.map { it.yieldTonPerAcre.toFloat() })

        Spacer(modifier = Modifier.height(12.dp))

        // Continue Button inside bottom sheet
        Button(
            onClick = onProceed,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
        ) {
            Text("Looks Good – Next", color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = dmSans, fontSize = 16.sp)
        }

        // Gentle friendly note for confidence
        Text(
            text = "Not sure? You can change crops later. FarmOS + DhartiMitra will guide you for the best results.",
            color = Color(0xFF8A7A3F),
            fontFamily = dmSans,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
