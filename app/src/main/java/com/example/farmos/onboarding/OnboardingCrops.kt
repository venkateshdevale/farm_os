package com.example.farmos.onboarding

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.farmos.Data.OnboardingViewModel
import com.example.farmos.R
import com.example.farmos.ViewModel.CropSuggestionViewModel
import com.example.farmos.data.mockWeatherData
import com.example.farmos.nav
import com.example.farmos.network.CropInfo
import com.example.farmos.ui.components.MPBarChart
import com.example.farmos.ui.components.MPLineChart
import com.example.farmos.ui.components.MPPieChart
import kotlinx.coroutines.delay
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingCropSuggestScreen(
    navController: NavController,
    onboardingViewModel: OnboardingViewModel,
    cropSuggestionViewModel: CropSuggestionViewModel = viewModel()
) {

    val dmSans = FontFamily(Font(R.font.dm_sans))
    var selectedCrop by remember { mutableStateOf<CropInfo?>(null) }
    var infoCrop by remember { mutableStateOf<CropInfo?>(null) }

    val cropSuggestionResponse by cropSuggestionViewModel.cropSuggestion.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    Log.d("SHABAZ", "OnboardingCropSuggestScreen isLoading")
    var suggestedCropsList by remember { mutableStateOf<List<CropInfo>>(emptyList()) }


    LaunchedEffect(Unit) {
//        Toast.makeText(LocalContext., "SHAACX", Toast.LENGTH_SHORT).show()
        Log.d("SHABAZ", "LAUNCHING getCropSuggestion")
        // Assuming a default season and soil quality for now, or retrieve from onboardingViewModel if available
        val location = "${onboardingViewModel.district}, ${onboardingViewModel.state}"
        val soilQuality = "loamy" // Placeholder, replace with actual data if available
        val date = "26-07-2025" // Placeholder, replace with actual data if available
        val farm_area = onboardingViewModel.areaInAcres
        val previousCrops: List<String>? = listOf("SugarCane") // Placeholder, replace with actual data if available

        cropSuggestionViewModel.getCropSuggestion(location, soilQuality, date, farm_area, previousCrops)
    }

    LaunchedEffect(cropSuggestionResponse) {
        if (cropSuggestionResponse != null) {
            isLoading = false
            Log.d("CropSuggestionAPI", "Response: $cropSuggestionResponse")
            cropSuggestionResponse?.let {
                // Convert CropSuggestionResponse to List<CropSuggestion>
                suggestedCropsList = it.crops
                suggestedCropsList = suggestedCropsList.mapIndexed { i, crop ->
                    crop.copy(
                        url = mockWeatherData[i].iconUrl,
                        historicalTrend = mockWeatherData[i].historicalTrend
                    )
                }

            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(bottom = 74.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Farm summary card - clean, on top
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD8F5DC)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Your Farm Details",
                        fontWeight = FontWeight.Bold,
                        fontFamily = dmSans,
                        fontSize = 15.sp,
                        color = Color(0xFF2F4D1D)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("District: ${onboardingViewModel.district}", fontFamily = dmSans, fontSize = 12.sp, color = Color.Black)
                    Text("State: ${onboardingViewModel.state}", fontFamily = dmSans, fontSize = 12.sp, color = Color.Black)
                    Text("Area: ${"%.2f".format(onboardingViewModel.areaInAcres)} acres", fontFamily = dmSans, fontSize = 12.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(6.dp))

                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Recommended Crops For Basappa",
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF00695C)
            )
            Text(
                text = "No random picks! These crops are matched for YOUR land, local weather, and real price/yield trends. Tap each for proof.",
                fontFamily = dmSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )

            // Crop suggestions
            if (isLoading) {
                CropLoadingScreen(isLoading)
            } else {
                if (suggestedCropsList.isEmpty()) {
                    Text(
                        text = "No crop suggestions available.",
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    suggestedCropsList.forEach { cropInfo ->
                        CropCard(
                            crop = cropInfo,
                            isSelected = selectedCrop?.name == cropInfo.name,
                            onSelect = {
                                selectedCrop = cropInfo
                                infoCrop = cropInfo
                            },
                            dmSans = dmSans
                        )
                    }
                }
            }

            // Subtle prompt if none selected
            if (selectedCrop == null && !isLoading) {
                Text(
                    text = "Tip: Tap a crop to check the real mandi and rainfall data behind each choice.",
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
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
                        infoCrop = crop
                        onboardingViewModel.farmName = crop.name
                        navController.navigate(nav.onboardingConfirm)
                    },
                    dmSans = dmSans
                )
            }
        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CropLoadingScreen(isLoading: Boolean) {
        val loadingMessages = listOf(
            "Analyzing your farm land...",
            "Getting soil quality information...",
            "Fetching historical weather...",
            "Checking best crops for maximum profit..."
        )

        var step by remember { mutableStateOf(0) }

        // Progress through messages every 1.5s
        LaunchedEffect(Unit) {
            while (step < loadingMessages.size - 1) {
                delay(1500)
                step++
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.95f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF3F51B5),
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedContent(
                    targetState = loadingMessages[step],
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) with fadeOut(animationSpec = tween(300))
                    },
                    label = "loadingStep"
                ) { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2F4D1D)
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                    )
                }
            }
        }

}


@Composable
fun CropCard(
    crop: CropInfo,
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
                model = crop.url,
                contentDescription = crop.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(62.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(crop.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = dmSans)
                Text("Investment: ${crop.investment} ", fontFamily = dmSans, fontSize = 12.sp)
                Text("Profit: ₹${crop.profit}/ton", fontFamily = dmSans, fontSize = 12.sp)
            }
            InfoIconWithPopup(crop.reasoning)

        }
    }
}
@Composable
fun InfoIconWithPopup(text: String) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(
            Icons.Default.Info,
            contentDescription = "Info",
            tint = Color(0xFFD4AF37)
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Reasoning") },
            text = { Text(text) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
@Composable
fun CropInfoContent(crop: CropInfo, onProceed: () -> Unit, dmSans: FontFamily) {
    Log.d("Shabaz","CropInfoContent "+ crop.toString())
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
            crop.reasoning,
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